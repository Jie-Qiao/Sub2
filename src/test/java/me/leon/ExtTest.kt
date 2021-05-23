package me.leon

import me.leon.support.*
import org.junit.jupiter.api.Test
import java.io.File


class ExtTest {

    @Test
    fun encode() {
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
            "vmess://ew0KICAidiI6ICIyIiwNCiAgInBzIjogIvCfh7rwn4e4576O5Zu9IOKYhuKYhiAgMDEg4piGTlRU4piGICAgMS4y5YCN546HIiwNCiAgImFkZCI6ICJiai5rZWFpeXVuLnh5eiIsDQogICJwb3J0IjogIjMxMTAzIiwNCiAgImlkIjogIjQ0MTg5MzQxLTJjYzktM2JlOS1iYjEwLWMxMzVlOThjZDhlYiIsDQogICJhaWQiOiAiMiIsDQogICJzY3kiOiAiYXV0byIsDQogICJuZXQiOiAid3MiLA0KICAidHlwZSI6ICJub25lIiwNCiAgImhvc3QiOiAid3d3LmJhaWR1LmNvbSIsDQogICJwYXRoIjogIi92MnJheSIsDQogICJ0bHMiOiAiIiwNCiAgInNuaSI6ICIiDQp9"
        println(Parser.parseV2ray(url)?.toUri())

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
        val url =
            "trojan://4806bfec-c8ca-4688-b513-f6214ea52e58@32vus.ednovas.me:443#Relay_%F0%9F%87%BA%F0%9F%87%B8US-%F0%9F%87%BA%F0%9F%87%B8US_320\n"
        val url2 =
            "trojan://qN7AKCF3@t6.ssrsub.one:8443#Relay_%F0%9F%87%B7%F0%9F%87%BARU-%F0%9F%87%B7%F0%9F%87%BARU_192"
        println(Parser.parse(url2)?.toUri())
    }

    @Test
    fun queryParse() {
//        val q =
//            "obfsparam=ZG93bmxvYWQud2luZG93c3VwZGF0ZS5jb20&protoparam=MTQzMDczOkN6OGFPaQ&remarks=6aaZ5rivLUI&group=MQ"
//        println(q.queryParamMap())

        val q2 =
            "obfsparam=&protoparam=dC5tZS9TU1JTVUI&remarks=UmVsYXlf8J+HqPCfh6ZDQS3wn4eo8J+HpkNBXzQxOSB8IDMuNTNNYg&group="
        println(q2.queryParamMapB64())
    }


    @Test
    fun fileTest() {
        println(File("./").canonicalPath)
        println(this.javaClass.getResource(""))
        println(this.javaClass.getResource("/"))
        println(this.javaClass.classLoader.getResource(""))
        println(this.javaClass.classLoader.getResource("/"))
    }

    @Test
    fun sliceTest() {
        println(7.slice(3))
        println(9.slice(3))
    }

    val failIpPorts by lazy {
        socketfailed.readLines().toHashSet().also { println(it) }
    }
    val fails = mutableSetOf<String>()

    @Test
    fun pingTest() {
//        "wwws.baidu.com".ping(3000, { ip, _ ->
//            failIpPorts.contains(ip) || fails.contains(ip)
//        }) {
//            println("error $it")
//            fails.add(it)
//            socketfailed.writeLine(it)
//        }.also { println(it) }
        println("wwws.baidu.com".quickPing())
        println("wwws.baidu.com".quickPing())
        println("www.baidu.com".quickPing())

    }

    @Test
    fun socketTest() {
//        "wwws.baidu.com".connect(80, 3000, { ip, port ->
//            failIpPorts.contains(ip) || fails.contains("$ip:$port") || failIpPorts.contains("$ip:$port")
//        }) {
//            println("error $it")
//            fails.add(it)
//            socketfailed.writeLine(it)
//        }.also { println(it) }
        println("wwws.baidu.com".quickConnect(50))
        println("www.baidu.com".quickConnect(80))
        println("www.baidu.com".quickConnect(443))

    }
}
