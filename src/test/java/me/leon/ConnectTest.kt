package me.leon

import kotlinx.coroutines.async
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import me.leon.support.*
import org.junit.jupiter.api.Test

class ConnectTest {

    @Test
    fun connectTest() {
        println("www.baidu.com".connect())
        println("www.baidu.com".ping())
        println("www.baidu.com".connect(443))
    }

    @Test
    fun poolTest() {
        var poolSize: Int
        var resume = false
        var resumeIndex = 0
        val lastSub = "".also {
            if (it.isEmpty()) {
                NODE_OK.writeLine()
            }
        }

        Parser.parseFromSub(POOL)
            .also {
                poolSize = it.size
            }
            .asSequence()
            .filterIndexed { index, sub ->

                if (sub.info() == lastSub || index == resumeIndex) {
                    println("${"bypass".takeIf { index != 0 } ?: ""} ${index + 1}/$poolSize ")
                    resume = true
                    return@filterIndexed index == 0
                }
                if (resume) {
                    println("${index + 1}/$poolSize ${sub.info()}")
                    true
                } else {
                    false
                }
            }
            .map { it to it.SERVER.quickConnect(it.serverPort) }
            .filter { it.second > -1 }
            .forEach {
                println(it.first.info() + ":" + it.second)
                NODE_OK.writeLine(it.first.toUri())
            }
    }

    val dispatch = newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors() * 6, "pool")

    @Test
    fun poolTest2() {
        var poolSize: Int
        var resume = false
        var resumeIndex = 0
        val lastSub = "".also {
            if (it.isEmpty()) {
                NODE_OK.writeLine()
            }
        }

        runBlocking {
            Parser.parseFromSub(POOL)
                .also { poolSize = it.size }
                .filterIndexed { index, sub ->
                    if (sub.info() == lastSub || index == resumeIndex) {
                        println("${"bypass".takeIf { index != 0 } ?: ""} ${index + 1}/$poolSize ")
                        resume = true
                        return@filterIndexed index == 0
                    }
                    if (resume) {
                        println("${index + 1}/$poolSize ${sub.info()}")
                        true
                    } else {
                        false
                    }
                }
                .map { it to async(dispatch) { it.SERVER.quickConnect(it.serverPort, 2000) } }
                .filter { it.second.await() > -1 }
                .forEach {
                    println(it.first.info() + ":" + it.second)
                    NODE_OK.writeLine(it.first.toUri())
                }
        }


    }

    @Test
    fun poolPingTest() {
        var poolSize: Int
        var resume = false
        var resumeIndex = 200
        val lastSub = "".also {
            if (it.isEmpty()) {
                NODE_OK.writeLine()
            }
        }

        runBlocking {
            Parser.parseFromSub(POOL)
                .also {
                    poolSize = it.size
                }
                .filterIndexed { index, sub ->
                    if (sub.info() == lastSub || index == resumeIndex) {
                        println("${"bypass".takeIf { index != 0 } ?: ""} ${index + 1}/$poolSize ")
                        resume = true
                        return@filterIndexed index == 0
                    }
                    if (resume) {
                        println("${index + 1}/$poolSize ${sub.info()}")
                        true
                    } else {
                        false
                    }
                }
                .map { it to async(dispatch) { it.SERVER.quickPing(2000) } }
                .filter { it.second.await() > -1 }
                .forEach {
                    println(it.first.info() + ":" + it.second)
//                YamlTest.available.writeLine(it.first.toUri())
                }
        }
    }
}


