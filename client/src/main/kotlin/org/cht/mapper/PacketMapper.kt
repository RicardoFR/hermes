package org.cht.mapper

import org.cht.database.model.Packet
import org.cht.model.ProbeRequest
import org.cht.model.RawPacket
import org.cht.service.DistanceCalculatorService
import java.time.Instant

object PacketMapper {

    fun mapRaw(rawPacket: RawPacket): ProbeRequest {
        val splitPacket = rawPacket.fields
        return ProbeRequest(
            timestamp = Instant.now(),
            packetType = splitPacket[1],
            wlanSrc = splitPacket[2],
            wlanDst = splitPacket[3],
            wlanBssid = splitPacket[4],
            pktTypes = splitPacket[5].toInt(),
            phySignal = splitPacket[6].toInt(),
            wlanLen = splitPacket[7].toInt(),
            phyRate = splitPacket[8].toInt(),
            phyFreq = splitPacket[9].toInt(),
            wlanTsf = splitPacket[10],
            wlanEssid = splitPacket[11],
            wlanMode = splitPacket[12].toInt(),
            wlanChannel = splitPacket[13].toInt(),
            wlanWep = splitPacket[14].toInt(),
            wlanWpa = splitPacket[15].toInt(),
            wlanRsn = splitPacket[16].toInt(),
            ipSrc = splitPacket[17],
            ipDst = splitPacket[18],
        )
    }

    fun mapPacket(probeRequest: ProbeRequest): Packet {
        val mac = probeRequest.wlanSrc
        val frequencyMhz = probeRequest.phyFreq
        val signalLevelDb = probeRequest.phySignal
        val distanceMeters : Int  = DistanceCalculatorService.calculateDistance(
            levelInDb = signalLevelDb.toDouble(),
            freqInMHz = frequencyMhz.toDouble()
        ).toUInt().toInt()
        val timestamp = probeRequest.timestamp
        //val targetHost = probeRequest.wlanEssid
        val channel = probeRequest.wlanChannel
        //val vendor = MacVendorLookupService.getMacVendor(mac)

        return Packet(
            mac = mac,
            channel = channel,
            frequencyMhz = frequencyMhz,
            dBm = signalLevelDb,
            distanceM = distanceMeters,
            time = timestamp
        )
    }

}