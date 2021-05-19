package me.leon.coroutine

import kotlinx.coroutines.*
import me.leon.YamlTest
import me.leon.fails
import me.leon.support.readLines
import me.leon.support.writeLine
import org.junit.jupiter.api.Test
import java.net.InetAddress
import kotlin.system.measureTimeMillis

class CoroutineTest {
    @Test
    fun hello() {
        GlobalScope.launch {
            delay(2000)
            println("hello")
        }
        println("wait")
//        Thread.sleep(4000)
        runBlocking {
            delay(4000)
        }
    }

    @Test
    fun hello2() = runBlocking {
        launch {
            delay(2000)
            println("hello")
        }
        println("wait")
        delay(4000)
    }

    @Test
    fun hello3() = runBlocking {
        launch {
            delay(200)
            println("hello")
        }
        coroutineScope {

        }
        println("wait")
        delay(4000)
    }

    @Test

    fun asyncTest() {

        runBlocking {
            measureTimeMillis {
                doSth1()
                doSth2()
                println()
            }
            delay(1000)
        }

        println("__________________")

        runBlocking {
            measureTimeMillis {
                var r1 = async { doSth1() }
                var r2 = async { doSth2() }
                println("async complete ")
            }.also { println("__async $it") }
            delay(1000)
        }

        println("__________________")
        runBlocking {
            measureTimeMillis {
                var r1 = async { doSth1() }
                var r2 = async { doSth2() }
                println("async complete ${r1.await() + r2.await()}")
            }.also { println("async await $it") }
        }

    }

    @Test
    fun asyncBatchTest() {
//
//        runBlocking {
//            YamlTest.socketfailed.readLines()
//                .map { asyncPing(it.split(":")[0]) }
//                .map { it.await() }
//                .also {
//                    println(it)
//                }
//        }

        runBlocking {
            YamlTest.socketfailed.readLines()
                .map { async(dispatcher){it.split(":")[0].ping()} }
//                .map { asyncPing(it.split(":")[0]) }
                .map { it.await() }
                .also {
                    println(it)
                }
        }

//        measureTimeMillis {
//            (1..10).map {
//                asyncFun(it)
//            }
//        }.also { println("time: $it") }
    }

    fun asyncFun(index: Int) = GlobalScope.async {
        println(index)
        delay(300)
    }

    suspend fun doSth1(): Int {
        delay(300)
        println("doSth1")
        return 1
    }

    suspend fun doSth2(): Int {
        delay(200)
        println("doSth2")
        return 2
    }

    val dispatcher = newFixedThreadPoolContext(32, "pools")
    fun asyncPing(ip: String) = GlobalScope.async(dispatcher) {
        ip.ping()
    }

    suspend fun asyncPing2(ip: String) = coroutineScope {
        println("coroutineScope ${Thread.currentThread().name}")
        async {
            println("coroutineScope async ${Thread.currentThread().name}")
            ip.ping()
        }
    }

    fun String.ping(timeout: Int = 1000) =
        try {
            var start = System.currentTimeMillis()
            val reachable = InetAddress.getByName(this).isReachable(timeout)
            if (reachable) (System.currentTimeMillis() - start)
            else {
                println("$this unreachable")
                -1
            }

        } catch (e: Exception) {
            println("ping err $this")
            -1
        }

}

