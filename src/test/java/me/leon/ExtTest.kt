package me.leon

import me.leon.support.*
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL


class ExtTest {

    @Test
    fun encode() {
        println("E:\\gitrepo\\Sub\\src\\main\\java\\me\\leon\\Helo.java".readText())
//            println("https://blog.csdn.net/oschina_41790905/article/details/79475187".readFromNet())
        println("https://suo.yt/WtbjDPJ".readFromNet())
        println("你好Leon".b64Encode())

        println("5L2g5aW9TGVvbg==".b64Decode())
        println("你好Leon".urlEncode())
        println("%E4%BD%A0%E5%A5%BDLeon".urlDecode())
    }
}