package me.leon

import me.leon.support.Sumurai
import me.leon.support.b64Decode
import me.leon.support.fromJson
import me.leon.support.readFromNet
import org.junit.jupiter.api.Test
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class NetworkSubTest {
    @Test
    fun subParse() {
        val subUrl = "https://gitee.com/bujilangren/warehouse/raw/master/Skr%20HOHOHO.txt"
        val subUrlTr = "https://proxy.51798.xyz/trojan/sub"
        val subUrlV2 = "https://proxy.51798.xyz/vmess/sub"
        val subUrlSsr = "https://proxy.51798.xyz/ssr/sub"
        val subUrlSs = "https://proxy.51798.xyz/sip002/sub"

        listOf(
//            subUrl,
//            subUrlSs,
//            subUrlSsr,
//            subUrlTr,
//            subUrlV2
            "https://gitee.com/bujilangren/warehouse/raw/master/0523.txt",
            "https://gitee.com/bujilangren/warehouse/raw/master/2021-5-23-ss&vmess.txt"
        ).forEach {
            kotlin.runCatching {
                Parser.parseFromSub(it)
                    .joinToString(
//                        "|",
                        "\r\n",
                        transform = Sub::toUri
                    )
                    .also {
                        println("___________")
                        println(it)
                    }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    @Test
    fun parseNet() {
        val key = SimpleDateFormat("yyyyMMdd").format(Date()).repeat(4)
        "https://ghproxy.com/https://raw.githubusercontent.com/webdao/v2ray/master/nodes.txt".readFromNet()
            .b64Decode()
            .foldIndexed(StringBuilder()) { index, acc, c ->
                acc.also { acc.append((c.code xor key[index % key.length].code).toChar()) }
            }.also {
                println(it)
            }
            .split("\n")
            .also {
                println(it.joinToString("|"))
            }
    }

    @Test
    fun parseSumaraiVpn() {
        "https://server.svipvpn.com/opconf.json".readFromNet()
            .fromJson<Sumurai>()
            .data.items.flatMap { it.items }
            .map { it.ovpn.b64Decode() }
            .forEach {
                println(it)
            }
    }
}