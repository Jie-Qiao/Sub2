package me.leon.support

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*


fun String.readText() = File(this).readText()
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
fun String.b64Encode() = Base64.getEncoder().encodeToString(this.toByteArray())
fun String.urlEncode() = URLEncoder.encode(this)
fun String.urlDecode() = URLDecoder.decode(this)
