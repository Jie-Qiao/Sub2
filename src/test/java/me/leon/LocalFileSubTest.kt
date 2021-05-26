package me.leon

import me.leon.domain.Panda
import me.leon.support.fromJson
import me.leon.support.readLines
import org.junit.jupiter.api.Test

class LocalFileSubTest {
    @Test
    fun readLocal() {
        Parser.parseFromSub("$ROOT\\V2RayN.txt")
            ?.joinToString("|") { it.toUri() }
            .also { println(it) }
    }

    @Test
    fun readLocal2() {
        Parser.parseFromSub("$ROOT\\subs.txt")
            ?.joinToString("\n") { it.toUri() }
            .also { println(it) }
    }

    @Test
    fun readLocal3() {
        Parser.parseFromSub("$ROOT\\bihai.yaml")
            ?.joinToString("\n") { it.info() }
            .also { println(it) }
    }

    @Test
    fun parsePanda() {
        "$ROOT/panda.txt".readLines().map {
            with(it.fromJson<Panda>()) {
                SS(authscheme, password, host, port.toString()).apply {
                    remark = this@with.name
                }
            }
        }
            .also { println(it.joinToString("\n") { it.toUri() }) }
    }
}