package com.kylecorry.trailsensecore.domain.math

import kotlin.math.*

fun normalizeAngle(angle: Float): Float {
    return wrap(angle, 0f, 360f) % 360
}

fun normalizeAngle(angle: Double): Double {
    return wrap(angle, 0.0, 360.0) % 360
}

fun wrap(value: Float, min: Float, max: Float): Float {
    return wrap(value.toDouble(), min.toDouble(), max.toDouble()).toFloat()
}

fun wrap(value: Double, min: Double, max: Double): Double {
    val range = max - min

    var newValue = value

    while (newValue > max) {
        newValue -= range
    }

    while (newValue < min) {
        newValue += range
    }

    return newValue
}

fun sinDegrees(angle: Double): Double {
    return sin(angle.toRadians())
}

fun tanDegrees(angle: Double): Double {
    return tan(angle.toRadians())
}

fun tanDegrees(angle: Float): Float {
    return tan(angle.toRadians())
}

fun cosDegrees(angle: Double): Double {
    return cos(angle.toRadians())
}

fun Double.toRadians(): Double {
    return Math.toRadians(this)
}

fun Float.toRadians(): Float {
    return Math.toRadians(this.toDouble()).toFloat()
}

fun deltaAngle(angle1: Float, angle2: Float): Float {
    var delta = angle2 - angle1
    delta += 180
    delta -= floor(delta / 360) * 360
    delta -= 180
    if (abs(abs(delta) - 180) <= Float.MIN_VALUE) {
        delta = 180f
    }
    return delta
}

fun clamp(value: Float, minimum: Float, maximum: Float): Float {
    return min(maximum, max(minimum, value))
}

fun Double.roundPlaces(places: Int): Double {
    return (this * 10.0.pow(places)).roundToInt() / 10.0.pow(places)
}

fun Float.roundPlaces(places: Int): Float {
    return (this * 10f.pow(places)).roundToInt() / 10f.pow(places)
}

fun Float.toDegrees(): Float {
    return Math.toDegrees(this.toDouble()).toFloat()
}

fun Double.toDegrees(): Double {
    return Math.toDegrees(this)
}

fun smooth(data: List<Float>, smoothing: Float = 0.5f): List<Float> {
    if (data.isEmpty()) {
        return data
    }

    val filter = LowPassFilter(smoothing, data.first())

    return data.mapIndexed { index, value ->
        if (index == 0) {
            value
        } else {
            filter.filter(value)
        }
    }
}

fun movingAverage(data: List<Float>, window: Int = 5): List<Float> {
    val filter = MovingAverageFilter(window)

    return data.map { filter.filter(it.toDouble()).toFloat() }
}

/**
 * Calculates the slope of the best fit line
 */
fun slope(data: List<Pair<Float, Float>>): Float {
    if (data.isEmpty()) {
        return 0f
    }

    val xBar = data.map { it.first }.average().toFloat()
    val yBar = data.map { it.second }.average().toFloat()

    var ssxx = 0.0f
    var ssxy = 0.0f
    var ssto = 0.0f

    for (i in data.indices) {
        ssxx += (data[i].first - xBar).pow(2)
        ssxy += (data[i].first - xBar) * (data[i].second - yBar)
        ssto += (data[i].second - yBar).pow(2)
    }

    return ssxy / ssxx
}