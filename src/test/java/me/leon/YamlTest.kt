package me.leon


import me.leon.support.readFromNet
import me.leon.support.readLines
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
        val pool2 = "$ROOT\\pools2.txt"
        val speed = "$ROOT\\speedtest.txt"
        val share = "$ROOT\\share.txt"
        val available = "$ROOT\\available.txt"
    }

    @Test
    fun yamlTest() {

        val curList = Parser.parseFromSub(pool).also {
            println(it.size)
        }
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
        var subs = "$ROOT\\sublist".readLines()
        pool2.writeLine()
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
        Parser.parseFromSub(pool2).also { println(it.size) }.sortedBy { it.toUri() }.map {
            pool.writeLine(it.toUri())
        }
    }

    @Test
    fun speedTest() {
        val map = Parser.parseFromSub(available).also { println(it.size) } .fold(mutableMapOf<String, Sub>()) { acc, sub ->
            acc.apply { acc[sub.name] = sub }
        }
//        println(map)
        share.writeLine()
        speed.readLines()
            .map { it.slice(0 until it.lastIndexOf('|')) to  it.slice(it.lastIndexOf('|')+1 until  it.length) }
            .sortedBy { -it.second.replace("Mb|MB".toRegex(), "").toFloat() }
//            .forEach {
//                println(it)
//            }
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
