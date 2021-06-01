package me.leon

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.leon.support.*
import org.junit.jupiter.api.Test

class NodeCrawler {
    /**
     * 1.爬取配置文件对应链接的节点,并去重
     * 2.同时进行可用性测试 tcping
     */
    @Test
    fun crawl() {
        //1.爬取配置文件的订阅
        crawlNodes()
        checkNodes()
    }

    /**
     * 爬取配置文件数据，并去重写入文件
     */
    private fun crawlNodes() {
        val subs1 = "$ROOT\\sublist".readLines()
        val subs2 = "$ROOT\\sublist_tmp".readLines()
        val subs = subs1 + subs2
        POOL.writeLine()
        println("共有订阅源：${subs.size}")

        runBlocking {
            subs.map { sub ->
                sub to async(DISPATCHER) {
                    Parser.parseFromSub(sub).also { println("$sub ${it.size} ") }
                }
            }
                .map { it.second.await() }
                .fold(linkedSetOf<Sub>()) { acc, linkedHashSet ->
                    acc.apply { acc.addAll(linkedHashSet) }
                }.sortedBy { it.toUri() }.also {
                    println("共有节点 ${it.size}")
                    POOL.writeLine(it.joinToString("\n") { it.toUri() })
                }
        }

    }

    /**
     * 节点可用性测试
     */
    private fun checkNodes() {
        //2.筛选可用节点
        var poolSize: Int
        var resume = false
        val resumeIndex = 0
        val lastSub = "".also {
            if (it.isEmpty()) {
                NODE_OK.writeLine()
            }
        }

        runBlocking {
            Parser.parseFromSub(POOL)
                .also { poolSize = it.size }
                .filterIndexed { index, sub ->
                    if (sub.info() == lastSub || index == resumeIndex) {
                        println("${"bypass".takeIf { index != 0 } ?: ""} ${index + 1}/$poolSize ")
                        resume = true
                        return@filterIndexed index == 0
                    }
                    if (resume) {
                        println("${index + 1}/$poolSize ${sub.info()}")
                        true
                    } else {
                        false
                    }
                }
                .map { it to async(DISPATCHER) { it.SERVER.quickConnect(it.serverPort, 2000) } }
                .filter { it.second.await() > -1 }
                .also { println("有效节点数量 ${it.size}") }
                .forEach {
//                    println(it.first.info())
                    NODE_OK.writeLine(it.first.toUri())
                }
        }
    }

    @Test
    fun nodeGroup() {
        NODE_SS.writeLine()
        NODE_SSR.writeLine()
        NODE_V2.writeLine()
        NODE_TR.writeLine()
        Parser.parseFromSub(NODE_OK).groupBy { it.javaClass }.forEach { (t, u) ->
            val data = u.joinToString("\n") { it.toUri() }.b64Encode()
            when (t) {
                SS::class.java -> NODE_SS.writeLine(data)
                    .also { println("ss节点: ${u.size}") }
                SSR::class.java -> NODE_SSR.writeLine(data)
                    .also { println("ssr节点: ${u.size}") }
                V2ray::class.java -> NODE_V2.writeLine(data)
                    .also { println("v2ray节点: ${u.size}") }
                Trojan::class.java -> NODE_TR.writeLine(data)
                    .also { println("trojan节点: ${u.size}") }
            }
        }
    }

    @Test
    fun nodeNationGroup() {
        Parser.parseFromSub(NODE_OK).groupBy { it.SERVER.ipCountryZh() }
            .forEach { (t, u) ->
                println("$t: ${u.size}")
                if (t == "UNKNOWN") println(u.map { it.SERVER })
            }
    }

    /**
     * 上面筛好节点后,进行第三方或者本地节点测速
     * 进行节点分组
     * 测速地址
     *  - http://gz.cloudtest.cc/
     *  - http://a.cloudtest.icu/
     */
    @Test
    fun availableSpeedTest() {
        Parser.parseFromSub(NODE_OK).chunked(60).map {
            it.map(Sub::toUri).also { println(it.joinToString("|")) }
        }
    }

    /**
     *，将网站测速后的结果复制到 speedtest.txt
     *  F12 控制台输入以下内容,提取有效节点信息,默认提取速度大于1MB/s的节点
     * <code>
     *     var rrs=document.querySelectorAll("tr.el-table__row");var ll=[];for(var i=0;i<rrs.length;i++){console.log("____");if(rrs[i].children[4].innerText.indexOf("MB")>0&& Number(rrs[i].children[4].innerText.replace("MB","")) >1){ll.push(rrs[i].children[1].innerText+"|" +rrs[i].children[4].innerText);}};ll.join("\n");
     * </code>
     * 最后进行分享链接生成
     */
    @Test
    fun speedTestResultParse() {
        val map =
            Parser.parseFromSub(NODE_OK)
                .also { println(it.size) }
                .fold(mutableMapOf<String, Sub>()) { acc, sub ->
                    acc.apply { acc[sub.name] = sub }
                }
        NODE_SS2.writeLine()
        NODE_SSR2.writeLine()
        NODE_V22.writeLine()
        NODE_TR2.writeLine()
        SPEED_TEST_RESULT.readLines()
            .distinct()
            .map { it.substringBeforeLast('|') to it.substringAfterLast('|') }
            .sortedByDescending { it.second.replace("Mb|MB".toRegex(), "").toFloat() }
            .filter { map[it.first] != null }
            .groupBy { map[it.first]!!.javaClass }
            .forEach { (t, u) ->
                val data = u.joinToString("\n") {
                    map[it.first]!!.apply {
                        name = name.substringBeforeLast('|') + "|" + it.second
                    }.toUri()
                }
                    .b64Encode()
                when (t) {
                    SS::class.java -> NODE_SS2.writeLine(data)
                        .also { println("ss节点: ${u.size}") }
                    SSR::class.java -> NODE_SSR2.writeLine(data)
                        .also { println("ssr节点: ${u.size}") }
                    V2ray::class.java -> NODE_V22.writeLine(data)
                        .also { println("v2ray节点: ${u.size}") }
                    Trojan::class.java -> NODE_TR2.writeLine(data)
                        .also { println("trojan节点: ${u.size}") }
                }
            }
    }
}