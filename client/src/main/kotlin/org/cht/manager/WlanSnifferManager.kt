package org.cht.manager

import mu.KotlinLogging
import org.cht.model.Daemon
import org.cht.model.NetworkCard

private val logger = KotlinLogging.logger {}

object WlanSnifferManager {

    fun startDaemon(networkCard: NetworkCard): Daemon {
        return runHorstProcess(networkCard.cardName)
            .also { logger.info {  "Starting tcpdump daemon for network -> ${networkCard.cardName}" }}
            .also { logger.info {  "Daemon running -> ${it.process.isAlive}" }}
    }

    /**
     * https://manpages.ubuntu.com/manpages/lunar/en/man8/horst.8.html
     */
    private fun runHorstProcess(
        wlanInterface: String
    ): Daemon {
        // -i -> Listen  on interface.
        // -I -> Put $wlanInterface in monitor mode
        // -e -> Print the link-level header on each dump line.
        val horstProcess = "sudo horst -i $wlanInterface -q -o /dev/stdout -f PROBRQ"
        // In case of Linux/Ubuntu run command using /bin/bash
        val linuxProcessBuilder = ProcessBuilder("/bin/bash", "-c", horstProcess)
        linuxProcessBuilder.redirectErrorStream(true)
        return Daemon(linuxProcessBuilder.start())

    }
}