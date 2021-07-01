package me.leon

import me.leon.NodeCrawler.Companion.REG_AD
import me.leon.domain.ClashConnectLog
import me.leon.support.*
import org.junit.jupiter.api.Test

class SpeedResultTest {

    @Test
    fun parseClashLog() {
        // clash_win/Cache 目录下日志文件
        val clashLogPath = "C:/Users/Leon/Desktop/f_00ce5e"
        clashLogPath.readText().fromJson<ClashConnectLog>()
            .proxies.filter { it.value.isNode && it.value.history.last().delay > 0 }
            .forEach { (t, u) ->
                println("${t.substring(5)}  ${u.history.last().delay}")
            }

    }

    @Test
    fun speedTestResultParse2() {
        val map =
            Parser.parseFromSub("C:/Users/Leon/Desktop/clash.txt")
                .also { println(it.size) }
                .fold(mutableMapOf<String, Sub>()) { acc, sub ->
                    acc.apply { acc[sub.name] = sub }
                }

        println(map)
        NODE_SS2.writeLine()
        NODE_SSR2.writeLine()
        NODE_V22.writeLine()
        NODE_TR2.writeLine()
        SPEED_TEST_RESULT.readLines()
            .asSequence()
            .distinct()
            .map { it.substringBeforeLast('|') to it.substringAfterLast('|') }
            .sortedByDescending { it.second.replace("Mb|MB".toRegex(), "").toFloat() }
            .filter { map[it.first] != null }
            .groupBy { map[it.first]!!.javaClass }
            .forEach { (t, u) ->
                val data = u.joinToString("\n") {
                    map[it.first]!!.apply {
                        name = name.replace(REG_AD, "").removeFlags().substringBeforeLast('|') + "|" + it.second
                    }.also { println(it.name) }.toUri()
                }
                    .b64Encode()
                when (t) {
                    SS::class.java -> NODE_SS2.writeLine(data)
                        .also { println("ss节点: ${u.size}") }
                    SSR::class.java -> NODE_SSR2.writeLine(data)
                        .also { println("ssr节点: ${u.size}") }
                    V2ray::class.java -> NODE_V22.writeLine(data)
                        .also { println("v2ray节点: ${u.size}") }
                    Trojan::class.java -> NODE_TR2.writeLine(data)
                        .also { println("trojan节点: ${u.size}") }
                }
            }
    }
}