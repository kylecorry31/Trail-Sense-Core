package com.kylecorry.trailsensecore.domain.weather

import com.kylecorry.trail_sense.weather.domain.HeatAlert
import com.kylecorry.trail_sense.weather.domain.HumidityComfortLevel
import java.time.Duration
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.pow

class WeatherService : IWeatherService {
    override fun getTendency(
        last: PressureReading,
        current: PressureReading,
        changeThreshold: Float
    ): PressureTendency {
        val diff = current.value - last.value
        val dt = Duration.between(last.time, current.time).seconds
        val changeAmt = (diff / dt) * 60 * 60 * 3

        val fastThreshold = changeThreshold + 2

        val characteristic = when {
            changeAmt <= -fastThreshold -> PressureCharacteristic.FallingFast
            changeAmt <= -changeThreshold -> PressureCharacteristic.Falling
            changeAmt >= fastThreshold -> PressureCharacteristic.RisingFast
            changeAmt >= changeThreshold -> PressureCharacteristic.Rising
            else -> PressureCharacteristic.Steady
        }

        return PressureTendency(characteristic, changeAmt)

    }

    override fun forecast(
        tendency: PressureTendency,
        currentPressure: PressureReading,
        stormThreshold: Float?
    ): Weather {
        val isStorm = tendency.amount <= (stormThreshold ?: 6f)

        if (isStorm) {
            return Weather.Storm
        }

        return when (tendency.characteristic) {
            PressureCharacteristic.FallingFast -> Weather.WorseningFast
            PressureCharacteristic.Falling -> Weather.WorseningSlow
            PressureCharacteristic.RisingFast -> Weather.ImprovingFast
            PressureCharacteristic.Rising -> Weather.ImprovingSlow
            else -> Weather.NoChange
        }
    }

    override fun convertToSeaLevel(
        reading: PressureAltitudeReading,
        temperature: Float?
    ): PressureReading {
        val pressure = if (temperature != null) {
            reading.pressure * (1 - ((0.0065f * reading.altitude) / (temperature + 0.0065f * reading.altitude + 273.15f))).pow(
                -5.257f
            )
        } else {
            reading.pressure * (1 - reading.altitude / 44330.0).pow(-5.255).toFloat()
        }

        return PressureReading(reading.time, pressure)
    }

    override fun classifyPressure(reading: PressureReading): PressureClassification {
        return when {
            reading.value >= 1022.689 -> PressureClassification.High
            reading.value <= 1009.144 -> PressureClassification.Low
            else -> PressureClassification.Normal
        }
    }

    override fun getHeatIndex(temperature: Float, relativeHumidity: Float): Float {
        if (temperature < 27) return temperature

        val c1 = -8.78469475556
        val c2 = 1.61139411
        val c3 = 2.33854883889
        val c4 = -0.14611605
        val c5 = -0.012308094
        val c6 = -0.0164248277778
        val c7 = 0.002211732
        val c8 = 0.00072546
        val c9 = -0.000003582

        val hi = c1 +
                c2 * temperature +
                c3 * relativeHumidity +
                c4 * temperature * relativeHumidity +
                c5 * temperature * temperature +
                c6 * relativeHumidity * relativeHumidity +
                c7 * temperature * temperature * relativeHumidity +
                c8 * temperature * relativeHumidity * relativeHumidity +
                c9 * temperature * temperature * relativeHumidity * relativeHumidity

        return hi.toFloat()
    }

    override fun getHeatAlert(heatIndex: Float): HeatAlert {
        return when {
            heatIndex <= -25 -> HeatAlert.FrostbiteDanger
            heatIndex <= -17 -> HeatAlert.FrostbiteWarning
            heatIndex <= 5 -> HeatAlert.FrostbiteCaution
            heatIndex < 27 -> HeatAlert.Normal
            heatIndex <= 32.5 -> HeatAlert.HeatCaution
            heatIndex <= 39 -> HeatAlert.HeatWarning
            heatIndex <= 50 -> HeatAlert.HeatAlert
            else -> HeatAlert.HeatDanger
        }
    }

    override fun getDewPoint(temperature: Float, relativeHumidity: Float): Float {
        val m = 17.62
        val tn = 243.12
        var lnRH = ln(relativeHumidity.toDouble() / 100)
        if (lnRH.isNaN() || abs(lnRH).isInfinite()) lnRH = ln(0.00001)
        val tempCalc = m * temperature / (tn + temperature)
        val top = lnRH + tempCalc
        var bottom = m - top
        if (bottom == 0.0) bottom = 0.00001
        val dewPoint = tn * top / bottom
        return dewPoint.toFloat()
    }

    override fun getHumidityComfortLevel(dewPoint: Float): HumidityComfortLevel {
        return when {
            dewPoint <= 55 -> HumidityComfortLevel.Pleasant
            dewPoint <= 60 -> HumidityComfortLevel.Comfortable
            dewPoint <= 65 -> HumidityComfortLevel.Sticky
            dewPoint <= 70 -> HumidityComfortLevel.Uncomfortable
            dewPoint <= 75 -> HumidityComfortLevel.Oppressive
            else -> HumidityComfortLevel.Miserable
        }
    }
}