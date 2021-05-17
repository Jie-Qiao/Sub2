package me.leon


import me.leon.support.readFromNet
import me.leon.support.readLines
import me.leon.support.writeLine
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class YamlTest {
    //        本地节点池
    val pool = "E:\\github\\Sub\\sub\\pools.txt"
    val pool2 = "E:\\github\\Sub\\sub\\pools2.txt"

    @Test
    fun yamlTest() {

        val curList = Parser.parseFromSub(pool).also {
            println(it.size)
        }
        var y = "E:\\github\\Sub\\sub\\sub.yml"
//        val url = "http://buliang0.tk/tool/freeproxy/05-06/clash-27.yml".readFromNet()
//        val url = "https://suo.yt/E80gdbo".readFromNet()
        val url = "https://www.233660.xyz/clash/proxies".readFromNet()
//        println("__ $url")
        if (url.isNotEmpty()) {
            with(Yaml(Constructor(Clash::class.java)).load(url) as Clash) {
                println(this.proxies.map(Node::node)
//                    .filterIsInstance<V2ray>()
                    .joinToString("|") { sub ->

                        sub?.also { println(it.info()) }?.toUri()
                            ?.also { if (!curList.contains(sub)) pool.writeLine(it) } ?: ""
                    })
            }
        } else {
            println("no content")
        }
    }

    @Test
    fun sublistParse() {
        var subs = "E:\\github\\Sub\\sub\\sublist".readLines()
        println(subs)
        subs.map { sub ->
            Parser.parseFromSub(sub).also { println("$sub ${it.size} ") }
        }.flatMap {
            it.map { it.toUri().also { pool2.writeLine(it) } }
        }
    }

    @Test

    fun pool() {
        pool.writeLine()
        pool2.readLines().also { println(it.size) }
        Parser.parseFromSub(pool2).also { println(it.size) }.map {
            pool.writeLine(it.toUri())
        }


    }
}
