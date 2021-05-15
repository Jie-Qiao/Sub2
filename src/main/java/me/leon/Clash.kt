package me.leon


data class Clash(
    var port: Int = 7890,
    var `socks-port`: Int = 7891,
    var `allow-lan`: Boolean = false,
    var `log-level`: String = "info",
    var `external-controller`: String = "info",
    var mode: String = "Rule",
    var `proxy-groups`: List<Group> = mutableListOf(),
    var proxies: List<Node> = mutableListOf(),
    var rules: List<String> = mutableListOf(),
)

data class Node(
    var name: String = "",
    var type: String = "",
    var cipher: String = "",
    var country: String = "",
    var obfs: String = "",
    var password: String = "",
    var port: Int = 0,
    var protocol: String = "",
    var uuid: String = "",
    var alterId: String = "",
    var network: String = "",
    var `protocol-param`: String = "",
    var server: String = "",
    var servername: String = "",

    ) {
    var `ws-headers`: LinkedHashMap<String, String> = linkedMapOf()
    var `http-opts`: LinkedHashMap<String, String> = linkedMapOf()
    var `h2-opts`: LinkedHashMap<String, String> = linkedMapOf()
    var `plugin-opts`: LinkedHashMap<String, String> = linkedMapOf()
    var `ws-path`: String = ""
    var `obfs-param`: String = ""
    var plugin: String = ""
    var udp: Boolean = false
    var tls: Boolean = false
    var `skip-cert-verify`: Boolean = false

    fun node(): Sub? {
        return when (type) {
            //            {"name":"Pool_🇦🇱AL_04","server":"31.171.154.221","type":"ss","country":"🇦🇱AL","port":39772,"password":"CUndSZnYsPKcu6Kj8THVMBHD","cipher":"aes-256-gcm"}
            "ss" -> SS(cipher, password, server, port.toString()).apply { remark = name }
            "ssr" -> SSR(
                server,
                port.toString(),
                protocol,
                cipher,
                obfs,
                password,
                if (obfs == "plain") "" else "",
                `protocol-param`
            ).apply { remarks = name }
//            TODO()
            "vmess" -> V2ray(
                aid = alterId,
                add = server,
                port = port.toString(),
                id = uuid,
                net = network,
                host = servername,
                tls = tls.toString()
            ).apply { ps = name }
//            {"name":"Relay_🇨🇦CA-🇨🇦CA_30","server":"t3.ssrsub.one","type":"trojan","country":"🇨🇦CA","port":443,"password":"a0Ndyox5","skip-cert-verify":true,"udp":true}
            "trojan" -> Trojan(password, server, port.toString()).apply { remark = name }
            else -> NoSub
        }
    }
}

data class Group(
    var name: String = "",
    var type: String = "",
    var url: String = "",
    var interval: Int = 0,
    var tolerance: Int = 0,
    var proxies: List<String> = mutableListOf(),
)
