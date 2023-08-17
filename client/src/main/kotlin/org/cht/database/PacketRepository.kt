package org.cht.database

import org.cht.database.model.Packet

interface PacketRepository {

    fun save(packet : Packet)
}