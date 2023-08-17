package org.cht

import mu.KotlinLogging
import org.cht.configuration.ClientConfiguration
import org.cht.database.PacketRepository
import org.cht.database.impl.InfluxDbPacketRepository
import org.cht.database.model.Packet
import org.cht.handler.PacketHandler
import org.cht.manager.CardManager
import org.cht.manager.PacketScannerManager
import org.cht.manager.WlanSnifferManager
import org.cht.model.Daemon
import org.cht.model.NetworkCard

private val logger = KotlinLogging.logger {}

fun main(args: Array<String?>) {

    try {

        val configuration: ClientConfiguration = ClientConfiguration.Builder()
            .fromAutoDetectCard()
            .fromProperties("config.properties")
            .fromArgs(args)
            .build()

        val packetRepository: PacketRepository = InfluxDbPacketRepository(
            clientConfiguration = configuration
        )

        val packetHandler = PacketHandler(
            packetRepository = packetRepository
        )

        val networkCard: NetworkCard = CardManager.startMonitorMode(
            networkCard = configuration.networkCardInterface
        )

        val monitorDaemon: Daemon = WlanSnifferManager.startDaemon(
            networkCard = networkCard
        )

        PacketScannerManager.startScanAndListen(
            daemon = monitorDaemon,
            probeCallback = { onProbe: Packet -> onProbe.also { packetHandler.handle(onProbe) } }
        )

    } catch (exception: Exception) {
        logger.error { exception }
    }

}

