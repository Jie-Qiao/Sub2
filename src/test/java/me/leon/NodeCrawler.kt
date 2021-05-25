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
        val subs = "$ROOT\\sublist".readLines()
        POOL.writeLine()
        println("共有订阅源：${subs.size}")
        subs.map { sub -> Parser.parseFromSub(sub).also { println("$sub ${it.size} ") } }
            .fold(linkedSetOf<Sub>()) { acc, linkedHashSet ->
                acc.apply { acc.addAll(linkedHashSet) }
            }.sortedBy { it.toUri() }.also {
                println("共有节点 ${it.size}")
                POOL.writeLine(it.joinToString("\n") { it.toUri() })
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
                .forEach {
                    println(it.first.info())
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
            when (t) {
                SS::class.java -> NODE_SS.writeLine(u.joinToString("\n") { it.toUri() }.b64Encode())
                    .also { println("ss节点: ${u.size}") }
                SSR::class.java -> NODE_SSR.writeLine(u.joinToString("\n") { it.toUri() }.b64Encode())
                    .also { println("ssr节点: ${u.size}") }
                V2ray::class.java -> NODE_V2.writeLine(u.joinToString("\n") { it.toUri() }.b64Encode())
                    .also { println("v2ray节点: ${u.size}") }
                Trojan::class.java -> NODE_TR.writeLine(u.joinToString("\n") { it.toUri() }.b64Encode())
                    .also { println("trojan节点: ${u.size}") }
            }
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
     *     var rrs=document.querySelectorAll("tr.el-table__row");var ll=[];for(var i=0;i<rrs.length;i++){console.log("____");if(rrs[i].children[4].innerText.indexOf("MB")>0&& Number(rrs[i].children[4].innerText.replace("MB","")) >2){ll.push(rrs[i].children[1].innerText+"|" +rrs[i].children[4].innerText);}};ll.join("\n");
     * </code>
     * 最后进行分享链接生成
     */
    @Test
    fun speedTestResultParse() {
        val map =
            Parser.parseFromSub(NODE_OK).also { println(it.size) }.fold(mutableMapOf<String, Sub>()) { acc, sub ->
                acc.apply { acc[sub.name] = sub }
            }
        SHARE_NODE.writeLine()
        SPEED_TEST_RESULT.readLines()
            .map { it.slice(0 until it.lastIndexOf('|')) to it.slice(it.lastIndexOf('|') + 1 until it.length) }
            .sortedBy { -it.second.replace("Mb|MB".toRegex(), "").toFloat() }
            .filter { map[it.first] != null }
            .forEach {
                map[it.first]?.apply {
                    name =
                        name.slice(0 until (name.lastIndexOf('|').takeIf { it != -1 } ?: name.length)) + "|" + it.second
                }?.toUri()?.also {
                    println(it)
                    SHARE_NODE.writeLine(it)
                }
            }
    }
}