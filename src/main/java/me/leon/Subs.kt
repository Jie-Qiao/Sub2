package me.leon

import me.leon.support.b64Encode
import me.leon.support.b64EncodeNoEqual
import me.leon.support.toJson
import me.leon.support.urlEncode

interface Uri {
    fun toUri(): String
    fun info(): String
}

sealed class Sub : Uri
object NoSub : Sub() {
    override fun toUri() = "nosub"
    override fun info() = "nosub"
}

data class V2ray(
    /**
     * address  服务器
     */
    val add: String = "",
    val port: String = "",
    /**
     * uuid
     */
    val id: String = "",
    /**
     * alertId
     */
    val aid: String = "0",
    val scy: String = "auto",
    /**
     * network
     */
    val net: String = "tcp",

    /**
     * 伪装域名
     */
    val host: String = "",
    /**
     * 伪装路径
     */
    val path: String = "",
    /**
     * 伪装类型 tcp/kcp/QUIC 默认none
     */
    val type: String = "none",
    val tls: String = "",
    val sni: String = "",

    ) : Sub() {
    var v: String = "2"
    var ps: String = ""
    override fun toUri() = "vmess://${this.toJson().b64Encode()}"
    override fun info() = "$ps vmess $add:$port"
}

data class SS(
    val method: String = "",
    val pwd: String = "",
    val server: String = "",
    val port: String = "",
) : Sub() {
    var remark: String = ""

    override fun toUri() = "ss://${"$method:${pwd}@$server:$port".b64Encode()}#${remark.urlEncode()}"
    override fun info() = "$remark ss $server:$port"
}

data class SSR(
    val server: String = "",
    val port: String = "",
    val protocol: String = "",
    val method: String = "",
    val obfs: String = "",
    val password: String = "",
    val obfs_param: String = "",
    val protocol_param: String = "",
) : Sub() {
    var remarks: String = ""
    var group: String = ""
    override fun toUri() =
        "ssr://${"$server:$port:$protocol:$method:$obfs:${password.b64Encode()}/?obfsparam=${obfs_param.b64EncodeNoEqual()}&protoparam=${protocol_param.b64EncodeNoEqual()}&remarks=${remarks.b64EncodeNoEqual()}&group=${group.b64EncodeNoEqual()}".b64Encode()}"

    override fun info() = "$remarks ssr $server:$port"

}

data class Trojan(
    val password: String = "",
    val server: String = "",
    val port: String = ""
) : Sub() {
    var remark: String = ""
    override fun toUri() = "trojan://${"${password}@$server:$port"}#${remark.urlEncode()}"
    override fun info() = "$remark trojan $server:$port"
}