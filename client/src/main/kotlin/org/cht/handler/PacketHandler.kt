package org.cht.handler

import org.cht.database.PacketRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.cht.database.model.Packet
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class PacketHandler(
    private val packetRepository: PacketRepository
) {

    fun handle(packet: Packet) {
        logger.info { Json.encodeToString(packet) }
        packetRepository.save(packet)
    }

}