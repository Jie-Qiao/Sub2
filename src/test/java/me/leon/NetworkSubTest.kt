package me.leon

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.leon.domain.Sumurai
import me.leon.support.*
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class NetworkSubTest {
    @Test
    fun subParse() {
        val subUrl =
            "https://cloudfront-cdn-hk-iplc1.com/sub/r/wqzCu8Kvw57Dk8OFw4ZnwqDCtsK3w4HCvMOcw53DgcK-Y8OQw6XCtQ==/"
        val subUrl2 =
            "https://paste.gg/p/anonymous/9f5941d417334473bbc104b4322b30b8/files/2171ef53e3cf41ffbe3201756231cc86/raw"
        val subUrlTr = "https://proxy.51798.xyz/trojan/sub"
        val subUrlV2 = "https://proxy.51798.xyz/vmess/sub"
        val subUrlSsr = "https://proxy.51798.xyz/ssr/sub"
        val subUrlSs = "https://proxy.51798.xyz/sip002/sub"
        val e = "https://gitee.com/bujilangren/warehouse/raw/master/0529.txt"

        listOf(
            e,
//            subUrl,
//            subUrlSs,
//            subUrl2,
//            subUrlSsr,
//            subUrlTr,
//            subUrlV2
//            "https://gitee.com/bujilangren/warehouse/raw/master/0523.txt",
//            "https://gitee.com/bujilangren/warehouse/raw/master/2021-5-23-ss&vmess.txt"
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

    /**
     * 去除推广
     */
    @Test
    fun subRemarkModify() {
//        val e = "https://zyzmzyz.netlify.app/Clash.yml"
//        val e = "https://www.linbaoz.com/clash/proxies"
        Parser.debug
        listOf(
//            "https://fu.stgod.com/clash/proxies",
//            "https://free.mengbai.cf/clash/proxies",
//            "https://emby.luoml.eu.org/clash/proxies",
            "https://paste.ee/r/UAGsg/0",
        )
            .forEach {
                kotlin.runCatching {
                    Parser.parseFromSub(it)
                        .joinToString(
//                        "|",
                            "\r\n"
                        ) {
                            it.apply { name = name.replace("\\([^)]+\\)".toRegex(), "") }.info()
                        }
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
    fun load() {
       "http://pan-yz.chaoxing.com/download/downloadfile?fleid=607981566887628800&puid=137229880".readFromNet()
           .also { println(it) }
           .split("\r\n|\n".toRegex())
           .forEach {
           println(it)
       }
    }


    @Test
    fun parseNet() {
        val key = SimpleDateFormat("yyyyMMdd").format(Date()).repeat(4)
        "https://ghproxy.com/https://raw.githubusercontent.com/webdao/v2ray/master/nodes.txt"
            .readFromNet()
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
        val speed = SPEED_TEST_RESULT.readLines().fold(mutableMapOf<String, String>()) { acc, s ->
            acc.apply {
                acc[s.substringBeforeLast('|')] = s.substringAfterLast('|')
            }
        }.also { println(it) }

        runBlocking {
            "https://server.svipvpn.com/opconf.json".readFromNet()
                .fromJson<Sumurai>()
                .data.items.flatMap { it.items }
                .mapNotNull { Parser.parse(it.ovpn.b64Decode()) }
                .map { it to async(DISPATCHER) { it.SERVER.connect(it.serverPort, 2000) } }
                .filter { it.second.await() > -1 }
//                .filter { speed.keys.contains(it.first.name) }
                .forEach {
                    println(it.first
//                        .apply { name = name.substringBeforeLast('|') + "|" + speed[name] }
                        .toUri())
                }
        }
    }
}