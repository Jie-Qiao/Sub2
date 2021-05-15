package me.leon

import org.junit.jupiter.api.Test

class LocalFileSubTest {
    @Test
    fun readLocal() {

        Parser.parseFromSub("E:\\github\\Sub\\sub\\ssr")
            .joinToString("\r\n") { it.toUri() }
            .also { println(it) }

    }
}