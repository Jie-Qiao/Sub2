package me.leon

import me.leon.support.*

object Parser {
    val URI_PATTERN = "(\\w+)://([^ #]+)(?:#([^#]+))?".toRegex()
    val SS_PATTERN = "([^:]+):([^@]+)@([^:]+):(\\w{1,5})".toRegex()

    fun parse(uri: String): Sub? {
        return when (URI_PATTERN.matchEntire(uri)!!.groupValues[1]) {
            "vmess" -> parseV2ray(uri)
            "ss" -> parseSs(uri)
            "ssr" -> parseSsr(uri)
            else -> NoSub
        } ?: NoSub
    }

    fun parseV2ray(uri: String): V2ray? {
        URI_PATTERN.matchEntire(uri)?.run {
            return groupValues[2].b64Decode().fromJson<V2ray>()
        } ?: return null
    }

    fun parseSs(uri: String): SS? {
        URI_PATTERN.matchEntire(uri)?.run {
            val remark = groupValues[3].urlDecode()
            groupValues[2].b64Decode()?.also {
                SS_PATTERN.matchEntire(it)?.run {
                    return SS(groupValues[1], groupValues[2], groupValues[3], groupValues[4]).apply {
                        this.remark = remark
                    }
                }
            }
        }
        return null
    }

    fun parseSsr(uri: String): SSR? {
        URI_PATTERN.matchEntire(uri)?.run {
            groupValues[2].b64SafeDecode().split(":").run {
//                println(this[5])
                "(\\w+)/\\?(.+)".toRegex().matchEntire(this[5])?.let {
//                    println(it.groupValues[1].b64Decode())
//                    println(it.groupValues[2].queryParamMap())
//                    println(it.groupValues[2].queryParamMapB64())
                    val q = it.groupValues[2].queryParamMapB64()
                    return SSR(
                        this[0], this[1], this[2], this[3], this[4],
                        it.groupValues[1].b64Decode(),
                        q["obfsparam"] ?: "",
                        q["protoparam"] ?: "",
                    ).apply {
                        remarks = q["remarks"] ?: ""
                        group = q["group"] ?: ""
                    }
                }
            }
        }
        return null
    }

    fun parseTrojan(uri: String) {

    }
}