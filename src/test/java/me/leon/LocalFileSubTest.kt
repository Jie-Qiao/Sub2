package me.leon

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.leon.domain.Panda
import me.leon.support.DISPATCHER
import me.leon.support.connect
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
    fun readLocal4() {
        Parser.parseFromSub(NODE_OK)
//            .filter { it.info().contains("http") }
            ?.joinToString("\n") { it.name }
            .also { println(it) }
    }

    @Test
    fun parsePanda() {
        runBlocking {
            "$ROOT/panda.txt".readLines().map {
                with(it.fromJson<Panda>()) {
                    SS(authscheme, password, host, port.toString()).apply {
                        remark = this@with.alternate
                    }
                }
            }.map { it to async(DISPATCHER) { it.server.connect(it.port.toInt()) } }
                .filter { it.second.await() > 0 }
                .map { it.first }
                .also { println(it.joinToString("\n") { it.toUri() }) }
        }
    }
}