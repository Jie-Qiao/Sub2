package me.leon.support

import java.io.File
import java.lang.Math.ceil
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*


fun String.readText() = File(this).canonicalFile.also { println(it.canonicalPath) }.takeIf { it.exists() }?.readText() ?: ""
fun String.writeLine(txt: String = "") =
    if (txt.isEmpty()) File(this).writeText("") else File(this).appendText("$txt\n")

fun String.readLines() = File(this).readLines()
fun String.readFromNet() = try {
    String(
        (URL(this)
            .openConnection().apply {
                setRequestProperty(
                    "user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"
                )
            } as HttpURLConnection).takeIf {
//            println("$this __ ${it.responseCode}")
            it.responseCode == 200
        }?.inputStream?.readBytes() ?: "".toByteArray()
    )
} catch (e: Exception) {
    println("$this read err ${e.message}")
    ""
}


fun String.b64Decode() = String(Base64.getDecoder().decode(this))
fun String.b64SafeDecode() =
    if (this.contains(":")) this
    else
        try {
            String(
                Base64.getDecoder().decode(
                    this.trim().replace("_", "/")
                        .replace("-", "+")
                )
            )
        } catch (e: Exception) {
            println("failed: $this ${e.message}")
            ""
        }


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
//                println(matchResult.groupValues[2].urlDecode().b64SafeDecode())
                acc[matchResult.groupValues[1]] =
                    matchResult.groupValues[2].urlDecode().replace(" ", "+").b64SafeDecode()
            }
        }


fun Int.slice(group: Int): MutableList<IntRange> {

    val slice = kotlin.math.ceil(this.toDouble() / group.toDouble()).toInt()
    return (0 until group).foldIndexed(mutableListOf<IntRange>()) { index, acc, i ->
        acc.apply {
            acc.add(slice * index until ((slice * (i + 1)).takeIf { group - 1 != index } ?: this@slice)
            )
        }
    }
}


fun <T> Any?.safeAs(): T? = this as? T
