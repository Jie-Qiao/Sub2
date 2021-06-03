package me.leon


import me.leon.support.readFromNet
import me.leon.support.readText
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class YamlTest {


    @Test
    fun yamlLocalTest() {

        with(Yaml(Constructor(Clash::class.java)).load(BIHAI.readText()) as Clash) {
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
        val curList = Parser.parseFromSub(POOL).also {
            println(it.size)
        }
//        val url = "http://buliang0.tk/tool/freeproxy/05-06/clash-27.yml".readFromNet()
//        val url = "https://suo.yt/E80gdbo".readFromNet()
//        val url = "https://www.233660.xyz/clash/proxies".readFromNet()
        var url =
            "https://pub-api-1.bianyuan.xyz/sub?target=clash&url=https%3A%2F%2Ffzusrs.xyz%2F%2Flink%2FtOHiqgwTT7OkFiDR%3Fsub%3D3%26extend%3D1&insert=false&emoji=true&list=false&tfo=false&scv=false&fdn=false&sort=false&new_name=true".readFromNet()
//        println("__ $url")
        url =
//            "https://raw.githubusercontent.com/freebaipiao/freebaipiao/main/freebaipiao.yaml"
            "https://update.glados-config.org/clash/85156/042d344/56490/glados.yaml"
            .readFromNet()
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
}
