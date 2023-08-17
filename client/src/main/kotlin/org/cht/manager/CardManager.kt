package org.cht.manager

import com.google.common.io.Resources
import org.cht.model.NetworkCard


object CardManager {

    private var wlanInterface: String = ""

    fun startMonitorMode(networkCard: String): NetworkCard {
        wlanInterface = networkCard
        val resource = Resources.getResource("script/start_monitor_mode.sh")
        ProcessBuilder(
            "/bin/bash",
            resource.path,
            wlanInterface
        ).start()
        Thread.sleep(2000)

        return NetworkCard(cardName = networkCard)
    }

    fun stopMonitorMode() {
        val resource = Resources.getResource("script/stop_monitor_mode.sh")
        ProcessBuilder(
            "/bin/bash",
            resource.path,
            wlanInterface
        ).start()
    }
}