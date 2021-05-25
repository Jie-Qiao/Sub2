package me.leon

import me.leon.support.b64Encode
import me.leon.support.b64EncodeNoEqual
import me.leon.support.toJson
import me.leon.support.urlEncode

interface Uri {
    fun toUri(): String
    fun info(): String
    var name: String
    val SERVER: String
    val serverPort: Int
}

sealed class Sub : Uri
object NoSub : Sub() {
    override fun toUri() = "nosub"
    override fun info() = "nosub"
    override var name: String
        get() = "nosub"
        set(value) {}
    override var serverPort = 80
    override val SERVER = "nosub"

}

data class V2ray(
    /**
     * address  服务器
     */
    var add: String = "",
    var port: String = "",
    /**
     * uuid
     */
    var id: String = "",
    /**
     * alertId
     */
    var aid: String = "0",
    var scy: String = "auto",
    /**
     * network
     */
    var net: String = "tcp",

    /**
     * 伪装域名
     */
    var host: String = "",
    /**
     * 伪装路径
     */
    var path: String = "",

    /**
     * 默认false,空串即可
     */
    var tls: String = "",
    var sni: String = "",
) : Sub() {
    var v: String = "2"
    var ps: String = ""

    /**
     * 伪装类型 tcp/kcp/QUIC 默认none
     */
    var type: String = "none"
    override fun toUri() = "vmess://${this.toJson().b64Encode()}"
    override fun info() = "$ps vmess $add:$port"
    override var name: String
        get() = ps
        set(value) {
            ps = value
        }
    override var serverPort: Int = 0
        get() = port.toInt()
    override val SERVER
        get() = add
}

data class SS(
    val method: String = "",
    val pwd: String = "",
    val server: String = "",
    val port: String = "",
) : Sub() {
    var remark: String = ""

    override fun toUri() =
        "ss://${"$method:${pwd}@$server:$port".b64Encode()}#${name.urlEncode()}"

    override fun info() = "$remark ss $server:$port"
    override var name: String
        get() = remark.takeUnless { it.isEmpty() } ?: "$server:$port-SS-${hashCode()}"
        set(value) {
            remark = value
        }
    override val serverPort
        get() = port.toInt()
    override val SERVER
        get() = server
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
        "ssr://${
            ("$server:$port:$protocol:$method:$obfs:${password.b64Encode()}" +
                    "/?obfsparam=${obfs_param.b64EncodeNoEqual()}" +
                    "&protoparam=${protocol_param.b64EncodeNoEqual()}" +
                    "&remarks=${remarks.b64EncodeNoEqual()}" +
                    "&group=${group.b64EncodeNoEqual()}")
                .b64Encode()
        }"

    override fun info() = "$remarks ssr $server:$port"
    override var name: String
        get() = remarks
        set(value) {
            remarks = value
        }
    override val serverPort
        get() = port.toInt()
    override val SERVER
        get() = server

}

data class Trojan(
    val password: String = "",
    val server: String = "",
    val port: String = ""
) : Sub() {
    var remark: String = ""
    var query: String = ""
    override fun toUri() = "trojan://${"${password}@$server:$port$params"}#${name.urlEncode()}"
    override fun info() =
        if (query.isNullOrEmpty()) "$name trojan $server:$port" else "$remark trojan $server:$port?$query"

    override var name: String
        get() = remark.takeUnless { it.isEmpty() } ?: "$server:$port-TR-${hashCode()}"
        set(value) {
            remark = value
        }
    private val params
        get() = if (query.isNullOrEmpty()) "" else "?$query"
    override val serverPort
        get() = port.toInt()
    override val SERVER
        get() = server
}