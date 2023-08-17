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

            else -> {
                logger.error { "Invalid packet : " + rawPacket.rawString }
            }
        }
    }

    private fun isValidProbeRequest(
        rawPacket: RawPacket
    ): Boolean {
        return rawPacket.rawString.contains("PROBRQ")
            .and(rawPacket.fields.size > 18)
    }
}