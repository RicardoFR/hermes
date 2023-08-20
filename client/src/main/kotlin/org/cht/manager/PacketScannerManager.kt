package org.cht.manager

import mu.KotlinLogging
import org.cht.database.model.Packet
import org.cht.mapper.PacketMapper
import org.cht.model.Daemon
import org.cht.model.RawPacket
import java.io.BufferedReader
import java.io.InputStreamReader

private val logger = KotlinLogging.logger {}

object PacketScannerManager {

    fun startScanAndListen(
        daemon: Daemon,
        probeCallback: (Packet) -> Packet
    ) {
        var rawPacket: String
        val stdInput = BufferedReader(InputStreamReader(daemon.process.inputStream))
        while (stdInput.readLine().also { rawPacket = it } != null) {
            handlePacket(
                rawPacket = RawPacket(rawPacket),
                probeCallback = probeCallback
            )
        }
    }

    private fun handlePacket(
        rawPacket: RawPacket,
        probeCallback: (Packet) -> Packet
    ) {
        when {
            isValidProbeRequest(rawPacket) -> probeCallback.invoke(
                rawPacket
                    .let { PacketMapper.mapRaw(it) }
                    .let { PacketMapper.mapPacket(it) }
            )

            isSetupConfig(rawPacket) -> logger.info { rawPacket.rawString }
            else -> {
                logger.error { "Error : " + rawPacket.rawString }
            }
        }
    }

    private fun isSetupConfig(rawPacket: RawPacket): Boolean {
        val rawString = rawPacket.rawString
        return when {
            rawString.contains("Set") -> true
            rawString.contains("/dev/stdout") -> true
            rawString.contains("BSSID") -> true
            else -> false
        }
    }


    private fun isValidProbeRequest(
        rawPacket: RawPacket
    ): Boolean {
        return rawPacket.rawString.contains("PROBRQ")
            .and(rawPacket.fields.size > 18)
    }
}