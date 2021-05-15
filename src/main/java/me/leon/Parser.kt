package me.leon

import me.leon.support.*

object Parser {
    private val REG_SCHEMA_HASH = "(\\w+)://([^ #]+)(?:#([^#]+))?".toRegex()
    private val REG_SCHEMA = "(\\w+)://.*".toRegex()
    private val REG_SS = "([^:]+):([^@]+)@([^:]+):(\\d{1,5})".toRegex()
    private val REG_SSR_PARAM = "([^/]+)/\\?(.+)".toRegex()
    private val REG_TROJAN = "([^@]+)@([^:]+):(\\d{1,5})".toRegex()

    private var debug = false

    fun parse(uri: String): Sub? {
//        println(uri.matches(URI_PAT))
        REG_SCHEMA.matchEntire(uri)?.run {
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
        REG_SCHEMA_HASH.matchEntire(uri)?.run {
            return groupValues[2].b64Decode().fromJson<V2ray>()
        } ?: return null
    }

    fun parseSs(uri: String): SS? {
        REG_SCHEMA_HASH.matchEntire(uri)?.run {
            val remark = groupValues[3].urlDecode()
            groupValues[2].b64Decode()?.also {
                REG_SS.matchEntire(it)?.run {
                    return SS(groupValues[1], groupValues[2], groupValues[3], groupValues[4]).apply {
                        this.remark = remark
                    }
                }
            }
        }
        return null
    }

    fun parseSsr(uri: String): SSR? {
        REG_SCHEMA_HASH.matchEntire(uri)?.run {
            groupValues[2].b64SafeDecode().split(":").run {
//                println(this[5])
                REG_SSR_PARAM.matchEntire(this[5])?.let {
//                    println("parse params ${it.groupValues[2]}")
                    val q = it.groupValues[2].queryParamMapB64()
//                    println("parse maps ${q}")
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
        REG_SCHEMA_HASH.matchEntire(uri)?.run {
            val remark = groupValues[3].urlDecode()
            groupValues[2]?.also {
                REG_TROJAN.matchEntire(it)?.run {
                    return Trojan(groupValues[1], groupValues[2], groupValues[3]).apply {
                        this.remark = remark
                    }
                }
            }
        }
        return null
    }

    private fun parseFromFileSub(path: String) =
        path.readText()
            .b64SafeDecode()
            .split("\r\n|\n".toRegex())
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { Pair(it, parse(it)) }
            .filter { it.second !is NoSub }
            .fold(linkedSetOf<Sub>()) { acc, sub ->
                sub.second?.let { acc.add(it) } ?: kotlin.run {
                    println("parse failed: $sub")
                }
                acc
            }

    private fun parseFromNetwork(url: String) =
        url.readFromNet()
//                    .also { println(it) }
            .b64SafeDecode()
            .also { println(it) }
            .split("\r\n|\n".toRegex())
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { parse(it.replace("/#", "#")) }
            .filter { it !is NoSub }
            .fold(linkedSetOf<Sub>()) { acc, sub ->
                sub?.let { acc.add(it) }
                acc
            }

    fun parseFromSub(uri: String) =
        when {
            uri.startsWith("http") -> parseFromNetwork(uri)
            uri.startsWith("/") -> parseFromFileSub(uri)
            else -> parseFromFileSub(uri)
        }

}