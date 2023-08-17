package org.cht.model

import kotlinx.serialization.Serializable
import org.cht.mapper.InstantSerializer
import java.time.Instant

@Serializable
data class ProbeRequest(
    @Serializable(with= InstantSerializer::class)
    val timestamp: Instant,
    val packetType: String,
    val wlanSrc: String,
    val wlanDst: String,
    val wlanBssid: String,
    val pktTypes: Int,
    val phySignal: Int,
    val wlanLen: Int,
    val phyRate: Int,
    val phyFreq: Int,
    val wlanTsf: String,
    val wlanEssid: String,
    val wlanMode: Int,
    val wlanChannel: Int,
    val wlanWep: Int,
    val wlanWpa: Int,
    val wlanRsn: Int,
    val ipSrc: String,
    val ipDst: String,
)