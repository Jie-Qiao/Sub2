package me.leon


import me.leon.support.readFromNet
import me.leon.support.readLines
import me.leon.support.readText
import me.leon.support.writeLine
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File

class YamlTest {

    companion object {

        val ROOT = File("sub").absolutePath

        //        本地节点池
        val pool = "$ROOT\\pools.txt"
        val bihai = "$ROOT\\bihai.yaml"
        val pool2 = "$ROOT\\pools2.txt"
        val speed = "$ROOT\\speedtest.txt"
        val share = "$ROOT\\share.txt"
        val available = "$ROOT\\available.txt"
        val socketfailed = "$ROOT\\socketfail.txt"
    }

    @Test
    fun yamlLocalTest() {

        with(Yaml(Constructor(Clash::class.java)).load(bihai.readText()) as Clash) {
            println(this.proxies.map(Node::node)
//                    .filterIsInstance<V2ray>()
                .joinToString("|") { sub ->
                    sub?.also {
//                        println(it.info())
                    }?.toUri().also {
//                        println(it)
                    } ?: ""
                })
        }
    }

    @Test
    fun yamlTest() {

        val curList = Parser.parseFromSub(pool).also {
            println(it.size)
        }
//        val url = "http://buliang0.tk/tool/freeproxy/05-06/clash-27.yml".readFromNet()
//        val url = "https://suo.yt/E80gdbo".readFromNet()
//        val url = "https://www.233660.xyz/clash/proxies".readFromNet()
        var url =
            "https://pub-api-1.bianyuan.xyz/sub?target=clash&url=https%3A%2F%2Ffzusrs.xyz%2F%2Flink%2FtOHiqgwTT7OkFiDR%3Fsub%3D3%26extend%3D1&insert=false&emoji=true&list=false&tfo=false&scv=false&fdn=false&sort=false&new_name=true".readFromNet()
//        println("__ $url")
        url = "https://raw.githubusercontent.com/freebaipiao/freebaipiao/main/freebaipiao.yaml".readFromNet()
        if (url.isNotEmpty()) {
            with(Yaml(Constructor(Clash::class.java)).load(url) as Clash) {
                println(this.proxies.map(Node::node)
//                    .filterIsInstance<V2ray>()
                    .joinToString("|") { sub ->

                        sub?.also { println(it.info()) }?.toUri()
                            ?.also {
//                                if (!curList.contains(sub)) pool.writeLine(it)
                            } ?: ""
                    })
            }
        } else {
            println("no content")
        }
    }

    /**
     *
     * 1.爬取配置文件的订阅
     */
    @Test
    fun sublistParse() {
        var subs = "$ROOT\\sublist".readLines()
        pool2.writeLine()
        println(subs)
        subs.map { sub ->
            Parser.parseFromSub(sub).also { println("$sub ${it.size} ") }
        }.flatMap {
            it.map {
                it.toUri().also {
                    pool2.writeLine(it)
                }
            }
        }
    }

    /**
     *
     * 2.去重
     */
    @Test
    fun pool() {
        pool.writeLine()
        pool2.readLines().also { println(it.size) }
        Parser.parseFromSub(pool2).also { println(it.size) }.sortedBy { it.toUri() }.map {
            pool.writeLine(it.toUri())
        }
    }


    /**
     * 3.生成测试分组，进行网页测速 （先进行筛选pool中可用节点）
     * 测速地址
     *  - http://gz.cloudtest.cc/
     *  - http://a.cloudtest.icu/
     */
    @Test
    fun availableSpeedTest() {
        Parser.parseFromSub(available).chunked(60).map {
            it.map(Sub::toUri).also { println(it.joinToString("|")) }
        }
    }

    /**
     *，将测速后的结果复制到 speedtest.txt
     * 最后进行分享链接生成
     */
    @Test
    fun speedTestResultParse() {
        val map =
            Parser.parseFromSub(available).also { println(it.size) }.fold(mutableMapOf<String, Sub>()) { acc, sub ->
                acc.apply { acc[sub.name] = sub }
            }
        share.writeLine()
        speed.readLines()
            .map { it.slice(0 until it.lastIndexOf('|')) to it.slice(it.lastIndexOf('|') + 1 until it.length) }
            .sortedBy { -it.second.replace("Mb|MB".toRegex(), "").toFloat() }
            .filter { map[it.first] != null }
            .forEach {
                map[it.first]?.apply {
                    name =
                        name.slice(0 until (name.lastIndexOf('|').takeIf { it != -1 } ?: name.length)) + "|" + it.second
                }?.toUri()?.also {
                    println(it)
                    share.writeLine(it)
                }
            }
    }
}
