package me.leon.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

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
}
