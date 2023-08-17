package org.cht.service

import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

class DistanceCalculatorService {
    companion object {

        /**
         * Calculates the estimated distance between the access point based on frequency and signal strength in db.
         * Based on the Free-space path loss equation at http://en.wikipedia.org/wiki/FSPL
         * @param levelInDb
         * @param freqInMHz
         * @return the distance in meters
         */
        fun calculateDistance(
            levelInDb: Double,
            freqInMHz: Double
        ): Int {
            val exp = (27.55 - (20 * log10(freqInMHz)) + abs(levelInDb)) / 20.0
            return 10.0.pow(exp).roundToInt()
        }
    }
}