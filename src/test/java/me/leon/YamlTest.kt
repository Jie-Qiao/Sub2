package me.leon

import me.leon.support.readFromNet
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class YamlTest {
    @Test
    fun yamlTest() {

        var y = "E:\\github\\Sub\\sub\\sub.yml"
//        val url = "http://buliang0.tk/tool/freeproxy/05-06/clash-27.yml".readFromNet()
        val url = "https://suo.yt/E80gdbo".readFromNet()

        with(Yaml(Constructor(Clash::class.java)).load(url) as Clash) {
            println(this.proxies.map(Node::node)
//                    .filterIsInstance<V2ray>()
                .joinToString("|") {

                    it?.also { println(it.info()) }?.toUri() ?: ""
                })
        }

    }
}