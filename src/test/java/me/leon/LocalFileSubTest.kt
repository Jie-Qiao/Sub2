package me.leon

import me.leon.support.readLines
import me.leon.support.readText
import org.junit.jupiter.api.Test

class LocalFileSubTest {
    @Test
    fun readLocal() {
        "E:\\github\\Sub\\sub\\subs.txt".readLines()

            .map {
//                println("___" + it)
                Parser.parse(it.replace("/#", "#")).also { println(it) }
            }
//            .filter { it !is NoSub }
            .fold(linkedSetOf<Sub>()) { acc, sub ->
                acc.also { acc.add(sub!!) }
            }.joinToString("|") { it.toUri() }
            .also { println(it) }

    }
}