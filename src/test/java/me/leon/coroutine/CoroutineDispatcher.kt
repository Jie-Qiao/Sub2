package me.leon.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class CoroutineDispatcher {
    @Test
    fun dispatcher() {
        runBlocking {
            launch(Dispatchers.IO) {
                println("IO ${Thread.currentThread().name}")
            }
            launch {
                println("inherit ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Unconfined) {
                println("Unconfined ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Default) {
                println("default ${Thread.currentThread().name}")
            }
            launch(newSingleThreadContext("leon")) {
                println("newSingleThreadContext ${Thread.currentThread().name}")
            }
        }

    }

    @Test
    fun dispatcher2() {
        runBlocking {
            println("runBlocking      : I'm working in thread ${Thread.currentThread().name}")
            launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
                println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
                delay(500)
                println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
            }
            launch { // context of the parent, main runBlocking coroutine
                println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
                delay(1000)
                println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
            }
        }
    }

}