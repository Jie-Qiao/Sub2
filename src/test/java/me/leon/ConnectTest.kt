package me.leon

import kotlinx.coroutines.async
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import me.leon.support.readLines
import me.leon.support.writeLine
import org.junit.jupiter.api.Test
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

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
                YamlTest.available.writeLine()
            }
        }

        Parser.parseFromSub(YamlTest.pool)
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
            .map { it to it.SERVER.connect(it.serverPort) }
            .filter { it.second > -1 }
            .forEach {
                println(it.first.info() + ":" + it.second)
                YamlTest.available.writeLine(it.first.toUri())
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
                YamlTest.available.writeLine()
            }
        }

        runBlocking {
            Parser.parseFromSub(YamlTest.pool)
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
                .map { it to async(dispatch) { it.SERVER.connect(it.serverPort,2000) } }
                .filter { it.second.await() > -1 }
                .forEach {
                    println(it.first.info() + ":" + it.second)
                    YamlTest.available.writeLine(it.first.toUri())
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
                YamlTest.available.writeLine()
            }
        }

       runBlocking {
           Parser.parseFromSub(YamlTest.pool)
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
               .map { it to async(dispatch) { it.SERVER.ping(2000) } }
               .filter { it.second.await() > -1 }
               .forEach {
                   println(it.first.info() + ":" + it.second)
//                YamlTest.available.writeLine(it.first.toUri())
               }
       }
    }

}

val failIpPorts by lazy {
    YamlTest.socketfailed.readLines().toHashSet().also { println(it) }
}

/**
 * ip + port 测试
 */
fun String.connect(port: Int = 80, timeout: Int = 1000) =
    if (failIpPorts.contains(this) || failIpPorts.contains("$this:$port")) {
        println("quick fail from cache")
        -1
    } else {
        try {
            var start = System.currentTimeMillis()
            Socket().connect(InetSocketAddress(this, port), timeout)
            System.currentTimeMillis() - start
        } catch (e: Exception) {
            YamlTest.socketfailed.writeLine("$this:$port")
            -1
        }
    }

val fails = mutableSetOf<String>()

/**
 * ping 测试
 */
fun String.ping(timeout: Int = 1000) =
    if (failIpPorts.contains(this) || fails.contains(this)) {
        println("fast failed")
        -1
    } else
        try {
            var start = System.currentTimeMillis()
            val reachable = InetAddress.getByName(this).isReachable(timeout)
            if (reachable) (System.currentTimeMillis() - start)
            else {
                println("$this unreachable")
                fails.add(this)
                YamlTest.socketfailed.writeLine(this)
                -1
            }

        } catch (e: Exception) {
            println("ping err $this")
            fails.add(this)
            YamlTest.socketfailed.writeLine(this)
            -1
        }

