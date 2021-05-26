package me.leon

import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader
import me.leon.GeoParser.countryReader
import me.leon.support.toFile
import me.leon.support.toInetAddress
import java.net.InetAddress

object GeoParser {
    //register at https://www.maxmind.com/, and download your file,you also can download from https://leon.lanzoui.com/i4XoWph8yaj
    // todo change it

    private const val geoDir = "C:\\Users\\Leon\\Desktop\\geo"
    private val dbFile = "$geoDir\\GeoLite2-City.mmdb".toFile()
    private val dbCountryFile = "$geoDir\\GeoLite2-Country.mmdb".toFile()

    val cityReader: DatabaseReader by lazy {
        DatabaseReader.Builder(dbFile).withCache(CHMCache()).build()
    }
    val countryReader: DatabaseReader by lazy {
        DatabaseReader.Builder(dbCountryFile).withCache(CHMCache()).build()
    }
}

fun String.ipCountryZh() = try {
    countryReader.country(this.toInetAddress()).country.names["zh-CN"]
} catch (e: Exception) {
    "UNKNOWN"
}

fun InetAddress.ipCountryZh() = kotlin.runCatching { countryReader.country(this).country.names["zh-CN"] }
    .onFailure { "UNKNOWN" }

fun String.ipCountryEn() = kotlin.runCatching { countryReader.country(this.toInetAddress()).country.isoCode }
    .onFailure { "UNKNOWN" }

fun InetAddress.ipCountryEn() = kotlin.runCatching { countryReader.country(this).country.isoCode }
    .onFailure { "UNKNOWN" }