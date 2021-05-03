package com.kylecorry.trailsensecore.infrastructure.text

import com.kylecorry.trailsensecore.domain.math.roundPlaces
import java.text.DecimalFormat
import java.util.concurrent.ConcurrentHashMap

object DecimalFormatter {

    private val formatterMap = ConcurrentHashMap<Int, DecimalFormat>()
    private val strictFormatterMap = ConcurrentHashMap<Int, DecimalFormat>()

    fun format(number: Number, decimalPlaces: Int, strict: Boolean = true): String {
        val n = number.toDouble().roundPlaces(decimalPlaces)
        val cache = if (strict) strictFormatterMap else formatterMap
        val existing = cache[decimalPlaces]
        if (existing != null){
            return existing.format(n)
        }
        if (decimalPlaces <= 0){
            val formatter = DecimalFormat("#")
            cache.putIfAbsent(0, formatter)
            return formatter.format(n)
        }

        val builder = StringBuilder(if (strict) "0." else "#.")
        for (i in 0 until decimalPlaces){
            builder.append(if (strict) '0' else '#')
        }

        val fmt = DecimalFormat(builder.toString())
        cache.putIfAbsent(decimalPlaces, fmt)
        return fmt.format(n)
    }
}