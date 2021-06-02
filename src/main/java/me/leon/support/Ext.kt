package me.leon.support

import kotlinx.coroutines.newFixedThreadPoolContext
import me.leon.FAIL_IPS
import java.io.DataOutputStream
import java.io.File
import java.lang.StringBuilder
import java.net.*
import java.nio.charset.Charset
import java.util.*
import kotlin.system.measureTimeMillis


fun String.readText(charset: Charset = Charsets.UTF_8) =
    File(this).canonicalFile.takeIf { it.exists() }?.readText(charset) ?: ""

fun String.writeLine(txt: String = "") =
    if (txt.isEmpty()) File(this).writeText("") else File(this).appendText("$txt\n")

fun String.readLines() = File(this).readLines()
fun String.readFromNet() = try {
    String(
        (URL(this)
            .openConnection().apply {
//                setRequestProperty("Referer", "https://pc.woozooo.com/mydisk.php")
                connectTimeout = 10000
                readTimeout = 10000
                setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9")
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

/**
 * ip + port 测试
 */

val Nop = { _: String, _: Int -> false }
fun String.connect(
    port: Int = 80,
    timeout: Int = 1000,
    cache: (ip: String, port: Int) -> Boolean = Nop,
    exceptionHandler: (info: String) -> Unit = {}
) =
    if (cache.invoke(this, port)) {
//        println("quick fail from cache")
        -1
    } else {
        try {
            measureTimeMillis {
                Socket().connect(InetSocketAddress(this, port), timeout)
            }
        } catch (e: Exception) {
            exceptionHandler.invoke("$this:$port")
            -1
        }
    }


/**
 * ping 测试
 */
fun String.ping(
    timeout: Int = 1000,
    cache: (ip: String, port: Int) -> Boolean = Nop,
    exceptionHandler: (info: String) -> Unit = {}
) =
    if (cache.invoke(this, -1)) {
        println("fast failed")
        -1
    } else
        try {
            val start = System.currentTimeMillis()
            val reachable = InetAddress.getByName(this).isReachable(timeout)
            if (reachable) (System.currentTimeMillis() - start)
            else {
                println("$this unreachable")
                exceptionHandler.invoke(this)
                -1
            }

        } catch (e: Exception) {
            println("ping err $this")
            exceptionHandler.invoke(this)
            -1
        }

val failIpPorts by lazy {
    FAIL_IPS.readLines().toHashSet().also { println(it) }
}
val fails = mutableSetOf<String>()
fun String.quickConnect(
    port: Int = 80,
    timeout: Int = 1000
) = this.connect(port, timeout, { ip, port ->
    failIpPorts.contains(ip) || fails.contains("$ip:$port") || failIpPorts.contains("$ip:$port")
}) {
//    println("error $it")
    fails.add(it)
    FAIL_IPS.writeLine(it)
}

fun String.quickPing(
    timeout: Int = 1000
) = this.ping(timeout, { ip, _ ->
    failIpPorts.contains(ip) || fails.contains(ip)
}) {
    println("error $it")
    fails.add(it)
    FAIL_IPS.writeLine(it)
}

val DISPATCHER = newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors() * 6, "pool")


fun String.toFile() = File(this)

fun String.post(params: MutableMap<String, String>)=

    try {
        val  p =  params.keys.foldIndexed(StringBuilder()) { index, acc, s ->
            acc.also {
                acc.append("${"&".takeUnless { index == 0 } ?: ""}$s=${params[s]}")
            }
        }.toString()
        String(
            URL(this)
                .openConnection().safeAs<HttpURLConnection>()?.apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    setRequestProperty("Referer", "https://pc.woozooo.com/mydisk.php")
                    setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9")
                    setRequestProperty("Content-Length", "${p.toByteArray().size}")
                    setRequestProperty(
                        "user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"
                    )
                    useCaches = false
                    doInput = true
                    doOutput = true

                    DataOutputStream(outputStream).use {
                        it.writeBytes(p)
                    }
                }?.takeIf {
//            println("$this __ ${it.responseCode}")
                    it.responseCode == 200
                }?.inputStream?.readBytes() ?: "".toByteArray()
        )
    } catch (e: Exception) {
        println("$this read err ${e.message}")
        ""
    }

