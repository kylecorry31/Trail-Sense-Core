package com.kylecorry.trailsensecore.domain.astronomy.moon

import com.kylecorry.trailsensecore.domain.math.cosDegrees
import com.kylecorry.trailsensecore.domain.math.normalizeAngle
import com.kylecorry.trailsensecore.domain.math.sinDegrees
import com.kylecorry.trailsensecore.domain.time.toUTCLocal
import java.time.ZonedDateTime
import kotlin.math.pow

internal class MoonPhaseCalculator {

    /**
     * Get the current phase of the moon
     * @return The moon phase
     */
    fun getPhase(time: ZonedDateTime = ZonedDateTime.now()): MoonPhase {
        val phaseAngle = getPhaseAngle(time)
        val illumination = getIllumination(phaseAngle)

        for (phase in MoonTruePhase.values()){
            if (phase.startAngle <= phaseAngle && phase.endAngle >= phaseAngle){
                return MoonPhase(phase, illumination)
            }

            // Handle new moon
            if (phase.startAngle >= phase.endAngle){
                if (phase.startAngle <= phaseAngle || phase.endAngle >= phaseAngle){
                    return MoonPhase(phase, illumination)
                }
            }
        }

        return MoonPhase(MoonTruePhase.New, illumination)
    }

    private fun getPhaseAngle(time: ZonedDateTime): Float {
        // Algorithm from: Astronomical Algorithms by Jean Meeus
        val JDE = JulianDayCalculator.calculate(time.toUTCLocal()) //Julian Ephemeris Day

        val T = (JDE - 2451545) / 36525.0

        val D =
            normalizeAngle(
                297.8501921 + 445267.1114034 * T - 0.0018819 * T.pow(
                    2
                ) + T.pow(3) / 545868 - T.pow(4) / 113065000
            )

        val M = normalizeAngle(
            357.5291092 + 35999.0502909 * T - 0.0001536 * T.pow(2) + T.pow(3) / 24490000
        )

        val Mp =
            normalizeAngle(
                134.9633964 + 477198.8675055 * T - 0.0087414 * T.pow(
                    2
                ) + T.pow(3) / 69699 - T.pow(4) / 14712000
            )

        val i =
            180 - D - 6.289 * sinDegrees(Mp) + 2.100 * sinDegrees(
                M
            ) - 1.274 * sinDegrees(2 * D - Mp) - 0.658 * sinDegrees(
                2 * D
            ) - 0.214 * sinDegrees(
                2 * Mp
            ) - 0.110 * sinDegrees(D)

        return (i.toFloat() + 180) % 360f
    }

    private fun getIllumination(phaseAngle: Float): Float {
        return ((1 + cosDegrees(phaseAngle.toDouble() - 180)) / 2).toFloat() * 100
    }

}