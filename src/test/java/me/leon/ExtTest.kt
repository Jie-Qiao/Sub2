package me.leon

import me.leon.support.*
import org.junit.jupiter.api.Test


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

    @Test
    fun v2rayTest() {
        val url =
            "vmess://ew0KICAidiI6ICIyIiwNCiAgInBzIjogIkhLIEVDMiAwOSIsDQogICJhZGQiOiAiMTguMTYzLjEwNS4yMTkiLA0KICAicG9ydCI6ICI0NDgiLA0KICAiaWQiOiAiY2E3ODQwNWUtZTU1NC0zY2QzLTliNmQtMmVjOThlMjFjOGYxIiwNCiAgImFpZCI6ICIxIiwNCiAgIm5ldCI6ICJ3cyIsDQogICJ0eXBlIjogIm5vbmUiLA0KICAiaG9zdCI6ICIiLA0KICAicGF0aCI6ICIvaGxzL2NjdHY1cGhkLm0zdTgiLA0KICAidGxzIjogIiINCn0="

        println(Parser.parseV2ray(url))
//        Ha.URI_PATTERN.matchEntire(url)?.run {
//            val v2ray = groupValues[2].b64Decode().fromJson<V2ray>()
//            println("${groupValues[1]}   $v2ray")
//            println(v2ray.toUri())
//        }
    }

    @Test
    fun ssTest() {
        val url =
            "ss://YWVzLTI1Ni1nY206bjh3NFN0bmJWRDlkbVhZbjRBanQ4N0VBQDE1NC4xMjcuNTAuMTM4OjMxNTcy#(%e5%b7%b2%e5%9d%9a%e6%8c%ba5%e5%a4%a9)%e5%8d%97%e9%9d%9e%e3%80%90%e5%88%86%e4%ba%ab%e6%9d%a5%e8%87%aaYoutube%e4%b8%8d%e8%89%af%e6%9e%97%e3%80%91"
        println(Parser.parseSs(url)?.toUri())

//        Ha.URI_PATTERN.matchEntire(url)?.run {
//            val remark = groupValues[3].urlDecode()
//            groupValues[2].b64Decode().also {
//                Ha.SS_PATTERN.matchEntire(it)?.run {
//                    val ss = SS(groupValues[1], groupValues[2], groupValues[3], groupValues[4]).apply {
//                        this.remark = remark
//                    }
//                    println(ss.toUri())
//                    println(ss.toUri2())
//                    println()
//                }
//            }
//
//        }

    }

    @Test
    fun ssrTest() {

        val url =
            "ssr://bnRlbXAxNi5ib29tLnBhcnR5OjIxMDAwOmF1dGhfYWVzMTI4X3NoYTE6YWVzLTI1Ni1jZmI6aHR0cF9zaW1wbGU6VldzNU1rTlQvP29iZnNwYXJhbT1aRzkzYm14dllXUXVkMmx1Wkc5M2MzVndaR0YwWlM1amIyMCZwcm90b3BhcmFtPU1UUXpNRGN6T2tONk9HRlBhUSZyZW1hcmtzPTZhYVo1cml2TFVJJmdyb3VwPU1R"

        val    url2 = "ssr://bjU3LmJvb20ucGFydHk6MjUwMDA6YXV0aF9hZXMxMjhfc2hhMTphZXMtMjU2LWNmYjpodHRwX3NpbXBsZTpWV3M1TWtOVC8_b2Jmc3BhcmFtPVpHOTNibXh2WVdRdWQybHVaRzkzYzNWd1pHRjBaUzVqYjIwJnByb3RvcGFyYW09TVRRek1EY3pPa042T0dGUGFRJnJlbWFya3M9NmFhWjVyaXZMVVEmZ3JvdXA9TVE"
        println(Parser.parseSsr(url2)?.toUri())
        //        Ha.URI_PATTERN.matchEntire(url)?.run {
//
////            println(groupValues[1])
//            groupValues[2].b64Decode().split(":").run {
//                println(this[5])
//                "(\\w+)/\\?(.+)".toRegex().matchEntire(this[5])?.let {
//                    println(it.groupValues[1].b64Decode())
//                    println(it.groupValues[2].queryParamMap())
//                    println(it.groupValues[2].queryParamMapB64())
//
//                }
//
////                SSR(this[0],this[1],this[2],this[3],this[4],this[5].b64Decode())
//            }
//
//
//        }
    }

    @Test
    fun queryParse() {
        val q =
            "obfsparam=ZG93bmxvYWQud2luZG93c3VwZGF0ZS5jb20&protoparam=MTQzMDczOkN6OGFPaQ&remarks=6aaZ5rivLUI&group=MQ"
        println(q.queryParamMap())
    }

    @Test
    fun fileParse() {
       val subs = "E:\\github\\Sub\\sub\\subs.txt".readText().split("\r\n").map { Parser.parse(it) }
            .fold(linkedSetOf<Sub>()){
                acc, sub -> acc.also { acc.add(sub!!) }
            }
        println(subs.map { it.toUri() }.joinToString("|"))
    }

}
