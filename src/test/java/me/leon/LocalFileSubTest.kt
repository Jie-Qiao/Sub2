package me.leon

import org.junit.jupiter.api.Test

class LocalFileSubTest {
    @Test
    fun readLocal() {
        Parser.parseFromSub("$ROOT\\V2RayN.txt")
            ?.joinToString("|") { it.toUri() }
            .also { println(it) }
    }
}