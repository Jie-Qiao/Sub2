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

    }

    @Test
    fun ssTest() {
        val url =
            "ss://YWVzLTI1Ni1nY206bjh3NFN0bmJWRDlkbVhZbjRBanQ4N0VBQDE1NC4xMjcuNTAuMTM4OjMxNTcy#(%e5%b7%b2%e5%9d%9a%e6%8c%ba5%e5%a4%a9)%e5%8d%97%e9%9d%9e%e3%80%90%e5%88%86%e4%ba%ab%e6%9d%a5%e8%87%aaYoutube%e4%b8%8d%e8%89%af%e6%9e%97%e3%80%91"
        println(Parser.parseSs(url)?.toUri())
    }

    @Test
    fun ssrTest() {
        val url =
            "ssr://bnRlbXAxNi5ib29tLnBhcnR5OjIxMDAwOmF1dGhfYWVzMTI4X3NoYTE6YWVzLTI1Ni1jZmI6aHR0cF9zaW1wbGU6VldzNU1rTlQvP29iZnNwYXJhbT1aRzkzYm14dllXUXVkMmx1Wkc5M2MzVndaR0YwWlM1amIyMCZwcm90b3BhcmFtPU1UUXpNRGN6T2tONk9HRlBhUSZyZW1hcmtzPTZhYVo1cml2TFVJJmdyb3VwPU1R"
        val url2 =
            "ssr://bjU3LmJvb20ucGFydHk6MjUwMDA6YXV0aF9hZXMxMjhfc2hhMTphZXMtMjU2LWNmYjpodHRwX3NpbXBsZTpWV3M1TWtOVC8_b2Jmc3BhcmFtPVpHOTNibXh2WVdRdWQybHVaRzkzYzNWd1pHRjBaUzVqYjIwJnByb3RvcGFyYW09TVRRek1EY3pPa042T0dGUGFRJnJlbWFya3M9NmFhWjVyaXZMVVEmZ3JvdXA9TVE"
        println(Parser.parseSsr(url2)?.toUri())
    }
 @Test
    fun trojanTest() {
     val url = "trojan://4806bfec-c8ca-4688-b513-f6214ea52e58@32vus.ednovas.me:443#Relay_%F0%9F%87%BA%F0%9F%87%B8US-%F0%9F%87%BA%F0%9F%87%B8US_320\n"
        val url2 = "trojan://qN7AKCF3@t6.ssrsub.one:8443#Relay_%F0%9F%87%B7%F0%9F%87%BARU-%F0%9F%87%B7%F0%9F%87%BARU_192"
            println(Parser.parse(url2)?.toUri())
    }

    @Test
    fun queryParse() {
        val q =
            "obfsparam=ZG93bmxvYWQud2luZG93c3VwZGF0ZS5jb20&protoparam=MTQzMDczOkN6OGFPaQ&remarks=6aaZ5rivLUI&group=MQ"
        println(q.queryParamMap())
    }

}
