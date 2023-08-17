package org.cht.model

data class RawPacket(
    val rawString: String
) {
    val fields: List<String> = rawString.split(", ")
}