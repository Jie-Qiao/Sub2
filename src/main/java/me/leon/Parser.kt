package me.leon

import me.leon.support.*

object Parser {
    val URI_PATTERN = "(\\w+)://([^ #]+)(?:#([^#]+))?".toRegex()
    val URI_PAT = "(\\w+)://.*".toRegex()
    val SS_PATTERN = "([^:]+):([^@]+)@([^:]+):(\\d{1,5})".toRegex()
    val TROJAN_PATTERN = "([^@]+)@([^:]+):(\\d{1,5})".toRegex()

    fun parse(uri: String): Sub? {
//        println(uri.matches(URI_PAT))
        URI_PAT.matchEntire(uri)?.run {
            return when (groupValues[1]) {
                "vmess" -> parseV2ray(uri)
                "ss" -> parseSs(uri)
                "ssr" -> parseSsr(uri)
                "trojan" -> parseTrojan(uri)
                else -> NoSub
            }
        }
//        println("${uri.length}  ${uri.isBlank()} " + URI_PAT.matches(uri))
        return NoSub
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
//                    println(it.groupValues[2])
                    val q = it.groupValues[2].urlDecode().queryParamMapB64()
                    return SSR(
                        this[0], this[1], this[2], this[3], this[4],
                        it.groupValues[1].b64SafeDecode(),
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

    fun parseTrojan(uri: String): Trojan? {
        URI_PATTERN.matchEntire(uri)?.run {
            val remark = groupValues[3].urlDecode()
            groupValues[2]?.also {
                TROJAN_PATTERN.matchEntire(it)?.run {
                    return Trojan(groupValues[1], groupValues[2], groupValues[3]).apply {
                        this.remark = remark
                    }
                }
            }
        }
        return null
    }
}