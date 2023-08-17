package org.cht.database.model

import kotlinx.serialization.Serializable
import org.cht.mapper.InstantSerializer
import java.time.Instant

@Serializable
data class Packet(
    val mac: String,
    val frequencyMhz: Int,
    val dBm: Int,
    val channel: Int,
    val distanceM: Int,
    @Serializable(with = InstantSerializer::class)
    val time: Instant
)
