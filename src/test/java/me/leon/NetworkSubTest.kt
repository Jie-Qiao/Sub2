package me.leon

import me.leon.support.b64Decode
import me.leon.support.b64SafeDecode
import me.leon.support.readFromNet
import org.junit.jupiter.api.Test

class NetworkSubTest {
    @Test
    fun subParse() {
        val subUrl = "https://gitee.com/bujilangren/warehouse/raw/master/Skr%20HOHOHO.txt"
        val subUrlTr = "https://proxy.51798.xyz/trojan/sub"
        val subUrlV2 = "https://proxy.51798.xyz/vmess/sub"
        val subUrlSsr = "https://proxy.51798.xyz/ssr/sub"
        val subUrlSs = "https://proxy.51798.xyz/sip002/sub"

        listOf(
            subUrl,
            subUrlSs,
            subUrlSsr,
            subUrlTr,
            subUrlV2
        ).forEach {
            kotlin.runCatching {
                it.readFromNet()
//                    .also { println(it) }
                    .b64SafeDecode()
//                    .also { println(it) }
                    .split("\r\n|\n".toRegex())
                    .asSequence()
                    .filter { it.isNotEmpty() }
                    .map { Parser.parse(it.replace("/#", "#")) }
                    .filter { it !is NoSub }
                    .fold(linkedSetOf<Sub>()) { acc, sub ->
                        acc.also { acc.add(sub!!) }
                    }
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
}