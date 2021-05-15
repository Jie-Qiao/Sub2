package me.leon

import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.TypeDescription
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File

class YamlTest {
    @Test
    fun yamlTest() {

        var y = "E:\\github\\Sub\\sub\\sub.yml"

        Constructor(Clash::class.java).apply {
            addTypeDescription(TypeDescription(Clash::class.java)
                .apply { putListPropertyType("proxies", Node::class.java) })
        }.run {
            with(Yaml(this).load(File(y).inputStream()) as Clash) {
                println(this.proxies.map(Node::node).joinToString("\r\n") {

                    it?.also { println(it.info()) }?.toUri() ?: ""
                })
            }
        }
    }
}