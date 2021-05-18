package me.leon

import me.leon.support.slice
import me.leon.support.writeLine
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket

class ConnectTest {

    @Test
    fun connectTest() {
        println("www.baidu.com".connect())
        println("www.baidu.com".connect(443))
    }

    @Test
    fun poolTest() {
        var poolSize: Int
        var resume = false
        var resumeIndex = 1
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
                    println("${"bypass".takeIf { index != 0 }?:""} ${index + 1}/$poolSize ")
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

    @Test
    fun poolTest2() {
        var poolSize = 0
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
            .filterIndexed { index, sub ->

                if (sub.info() == lastSub || index == resumeIndex) {
                    println("bypass ${index + 1}/$poolSize ")
                    resume = true
                    return@filterIndexed false
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
}

fun String.connect(port: Int = 80, timeout: Int = 1000) =
    try {
        var start = System.currentTimeMillis()
        Socket().connect(InetSocketAddress(this, port), timeout)
        System.currentTimeMillis() - start
    } catch (e: Exception) {
        -1
    }