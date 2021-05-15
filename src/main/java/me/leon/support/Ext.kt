package me.leon.support

import java.io.File
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*


fun String.readText() = File(this).readText()
fun String.readLines() = File(this).readLines()
fun String.readFromNet() = String(
    (URL(this)
        .openConnection().apply {
            setRequestProperty(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"
            )
        } as HttpURLConnection).inputStream.readBytes()
)

fun String.b64Decode() = String(Base64.getDecoder().decode(this))
fun String.b64SafeDecode() =
    String(
        Base64.getDecoder().decode(
            this.replace("_", "/")
                .replace("-", "+")
        )
    )


fun String.b64Encode() = Base64.getEncoder().encodeToString(this.toByteArray())
fun String.b64EncodeNoEqual() = Base64.getEncoder().encodeToString(this.toByteArray()).replace("=", "")
fun String.urlEncode() = URLEncoder.encode(this)
fun String.urlDecode() = URLDecoder.decode(this)

fun String.queryParamMap() =
    "(\\w+)=([^&]*)".toRegex().findAll(this)?.fold(mutableMapOf<String, String>()) { acc, matchResult ->
        acc.apply { acc[matchResult.groupValues[1]] = matchResult.groupValues[2] }
    }

fun String.queryParamMapB64() =
    "(\\w+)=([^&]*)".toRegex()
        .findAll(this)
        ?.fold(mutableMapOf<String, String>()) { acc, matchResult ->
            acc.apply {
                acc[matchResult.groupValues[1]] = matchResult.groupValues[2].b64SafeDecode()
            }
        }
