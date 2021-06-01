package me.leon.ip

import me.leon.FAIL_IPS
import me.leon.support.ping
import me.leon.support.readLines
import me.leon.support.writeLine
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class IpFilterTest {

    @Test
    fun failIp() {
        val okIps = mutableListOf<String>()
        val failIps = mutableListOf<String>()
        val total = mutableListOf<String>()

        measureTimeMillis {
            FAIL_IPS.readLines()
                .also { println("before ${it.size}") }
                .toHashSet()
                .sorted()
                .also {
                    total.addAll(it)
                    println("after ${it.size}")
                    FAIL_IPS.writeLine()
                    FAIL_IPS.writeLine(it.joinToString("\n"))
                }
                .groupBy { it.contains(':') }
                .also { map ->
                    map[true].also { println("带端口节点数量 ${it?.size}") }?.map { it.substringBeforeLast(':') to it }
                        ?.forEach { p ->
                            if (map[false]?.contains(p.first) == true) {
                                println(p.second)
                            } else {
                                if (okIps.contains(p.first) || failIps.contains(p.first)) {
                                    println("已存在")
                                    return@forEach
                                }
                                if (p.first.ping(2000) > -1)
                                    okIps.add(p.first)
                                else println(p.second.also { failIps.add(p.first) })
                            }
                        }
                }

            println(failIps)
            println(okIps)
            total
                .toHashSet()
                .also {
                    println("before ${it.size}")
                    it.removeAll(okIps)
                    it.addAll(failIps)
                }
                .filterNot { it.contains(":") && failIps.contains(it.substringBeforeLast(":")) }
                .sorted()
                .also {
                    FAIL_IPS.writeLine()
                    FAIL_IPS.writeLine(it.joinToString("\n"))
                    println("after ${it.size}")
                }
        }.also { println("time $it ms") }
    }
}