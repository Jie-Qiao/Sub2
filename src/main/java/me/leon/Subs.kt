package me.leon

import me.leon.support.b64Encode
import me.leon.support.b64EncodeNoEqual
import me.leon.support.toJson
import me.leon.support.urlEncode

interface Uri {
    fun toUri(): String
}

sealed class Sub : Uri
object NoSub : Sub() {
    override fun toUri() = "nosub"
}

data class V2ray(
    val add: String = "",
    val aid: String = "",
    val host: String = "",
    val id: String = "",
    val net: String = "",
    val path: String = "",
    val port: String = "",
    val type: String = "",
    val v: String = "",
    val tls: String = ""
) : Sub() {
    var ps: String = ""
    override fun toUri() = "vmess://${this.toJson().b64Encode()}"
}

data class SS(
    val method: String = "",
    val pwd: String = "",
    val server: String = "",
    val port: String = "",
) : Sub() {
    var remark: String = ""

    override fun toUri() = "ss://${"$method:${pwd}@$server:$port".b64Encode()}#${remark.urlEncode()}"
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

}

data class Trojan(
    val password: String = "",
    val server: String = "",
    val port: String = ""
) : Sub() {
    var remark: String = ""
    override fun toUri() = "trojan://${"${password}@$server:$port"}#${remark.urlEncode()}"
}