package me.leon.ip

import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader
import me.leon.FAIL_IPS
import me.leon.support.readLines
import me.leon.support.toFile
import me.leon.support.toInetAddress
import org.junit.jupiter.api.Test
import java.io.File
import java.net.InetAddress


class GeoTest {
    //register at https://www.maxmind.com/, and download your file,you also can download from https://leon.lanzoui.com/i4XoWph8yaj
    // todo change it
    val geoDir = "C:\\Users\\Leon\\Desktop\\geo"
    val dbFile = "$geoDir\\GeoLite2-City.mmdb".toFile()
    val dbCountryFile = "$geoDir\\GeoLite2-Country.mmdb".toFile()

    val cityReader: DatabaseReader by lazy {
        DatabaseReader.Builder(dbFile).withCache(CHMCache()).build()
    }
    val countryReader: DatabaseReader by lazy {
        DatabaseReader.Builder(dbCountryFile).withCache(CHMCache()).build()
    }

    @Test
    fun geoParse() {


        val ipAddress = "128.101.101.101".toInetAddress()

        val response = countryReader.city(ipAddress)
        println(countryReader.metadata)

        response.country.run {
            println("country isoCode: $isoCode name: $name name-zh: ${names["zh-CN"]}")
        }
        response.mostSpecificSubdivision.run {
            println("subdivision isoCode: $isoCode name: $name name-zh: ${names["zh-CN"]}")
        }
        response.run {
            println("city: $city , postal: $postal  location: $location")
        }

        println(cityReader.country(ipAddress).country.names["zh-CN"])

    }


    @Test
    fun ip_reader() {
        FAIL_IPS.readLines().forEach {
            """^(\d+(?:.\d+){3})(:\d+)?$""".toRegex().matchEntire(it)?.run {
                println(this.groupValues[1] to countryReader.country(this.groupValues[1].toInetAddress()).country.names["zh-CN"])
            }
//            println(reader2.country(InetAddress.getByName(it)).country.names["zh-CN"])
        }
    }
}