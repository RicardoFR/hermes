package org.cht.configuration

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.commons.io.IOUtils
import org.cht.manager.CardManager
import java.nio.charset.Charset
import java.util.*

private val logger = KotlinLogging.logger {}

class ClientConfiguration private constructor(
    val networkCardInterface: String,
    val dbHost: String,
    val dbToken: String,
    val dbBucket: String,
    val dbOrganization: String,
) {

    class Builder() {
        private lateinit var args: Array<String?>
        private lateinit var propertiesFile: String
        private var autoDetectCard = false

        fun fromArgs(args: Array<String?>) = apply { this.args = args }

        fun fromProperties(properties: String) = apply { this.propertiesFile = properties }

        fun fromAutoDetectCard() = apply { this.autoDetectCard = true }

        fun build(): ClientConfiguration {
            Runtime
                .getRuntime()
                .addShutdownHook(shutdownThread)
            return loadProperties(
                propertiesFile = this.propertiesFile,
                args = this.args.toList(),
                autoDetectCard = autoDetectCard
            )
        }

    }

    companion object {

        private val loader: ClassLoader = Thread.currentThread().contextClassLoader

        private fun loadProperties(
            propertiesFile: String,
            args: List<String?>,
            autoDetectCard: Boolean
        ): ClientConfiguration {

            loader
                .getResourceAsStream(propertiesFile)
                .use { input ->
                    val properties = Properties()

                    // load a properties file
                    properties.load(input)

                    val wlan: String = findCard(
                        args = args,
                        properties = properties,
                        autoDetectCard = autoDetectCard
                    )

                    logger.info { "Using card -> $wlan" }

                    return ClientConfiguration(
                        networkCardInterface = wlan,
                        dbHost = properties.getProperty("db.host"),
                        dbToken = properties.getProperty("db.token"),
                        dbBucket = properties.getProperty("db.bucket"),
                        dbOrganization = properties.getProperty("db.organization")
                    ).also { logger.info { "Configuration -> $it" } }
                }
        }

        private fun findCard(
            args: List<String?>,
            properties: Properties,
            autoDetectCard: Boolean
        ): String {
            val wlanCard: String = try {
                args[0].toString()
            }catch (e : Exception){
                properties.getProperty("wlan.network.interface")
            }
            val listOfCards = getListOfCards()
            return if (autoDetectCard) {
                if (getListOfCards().contains(wlanCard)) {
                    return wlanCard
                } else {
                    return listOfCards.find { it.contains("w") } ?: ""
                }
            } else {
                wlanCard
            }
        }

        private fun getListOfCards(): List<String> {
            val mutableList = mutableListOf<String>()
            runBlocking {
                val p1 = Runtime.getRuntime().exec(arrayOf("iw", "dev"))
                val input = p1.inputStream
                val p2 = Runtime.getRuntime().exec(arrayOf("grep", "Interface"))
                val output = p2.outputStream
                IOUtils.copy(input, output)
                output.close() // signals grep to finish
                IOUtils
                    .readLines(p2.inputStream, Charset.defaultCharset())
                    .forEach { rawPacket -> mutableList.add(rawPacket.split(" ")[1]) }
            }
            return mutableList;
        }

        private val shutdownThread: Thread = object : Thread() {
            override fun run() {
                CardManager.stopMonitorMode()
            }
        }
    }

}
