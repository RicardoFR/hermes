package org.cht.database.impl

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import com.influxdb.client.kotlin.WriteKotlinApi
import com.influxdb.client.write.Point
import org.cht.configuration.ClientConfiguration
import org.cht.database.PacketRepository
import org.cht.database.model.Packet
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.time.Instant

private val logger = KotlinLogging.logger {}

class InfluxDbPacketRepository(clientConfiguration: ClientConfiguration) : PacketRepository {

    // Replace the placeholder with your Atlas connection string
    private val serverURL: String = clientConfiguration.dbHost
    private val bucket = clientConfiguration.dbBucket
    private val token = clientConfiguration.dbToken
    private val organization = clientConfiguration.dbOrganization

    //private val influxDBClient = InfluxDBClientKotlinFactory.create(serverURL, username, password.toCharArray());
    private val influxDBClient = InfluxDBClientKotlinFactory.create(
        serverURL,
        token.toCharArray(),
        organization,
        bucket
    );

    private val writeApi: WriteKotlinApi = influxDBClient.getWriteKotlinApi()

    override fun save(packet: Packet) {
        val point = Point.measurement("packet")
            .addTag("mac", packet.mac)
            .addTag("frequencyMhz", packet.frequencyMhz.toString())
            .addTag("channel", packet.channel.toString())
            .addField("dBm", packet.dBm)
            .addField("distanceM", packet.distanceM)
            .time(Instant.now().toEpochMilli(), WritePrecision.MS)

        try {
            runBlocking { writeApi.writePoint(point, bucket, organization) }
        } catch (exception: Exception) {
            logger.error { exception }
        }
    }

}