package com.kylecorry.trailsensecore.infrastructure.persistence

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.kylecorry.trailsensecore.domain.geo.Coordinate
import com.kylecorry.trailsensecore.domain.math.toDoubleCompat
import java.time.Instant

class Cache(context: Context) {

    private val sharedPrefs by lazy { PreferenceManager.getDefaultSharedPreferences(context.applicationContext) }

    fun remove(key: String) {
        sharedPrefs?.edit { remove(key) }
    }

    fun removeCoordinate(key: String) {
        remove(key + "_latitude")
        remove(key + "_longitude")
    }

    fun contains(key: String): Boolean {
        return sharedPrefs?.contains(key) ?: false
    }

    fun containsCoordinate(key: String): Boolean {
        return contains(key + "_latitude") && contains(key + "_longitude")
    }

    fun putInt(key: String, value: Int) {
        sharedPrefs?.edit { putInt(key, value) }
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPrefs?.edit { putBoolean(key, value) }
    }

    fun putString(key: String, value: String) {
        sharedPrefs?.edit { putString(key, value) }
    }

    fun putFloat(key: String, value: Float) {
        sharedPrefs?.edit { putFloat(key, value) }
    }

    fun putDouble(key: String, value: Double) {
        sharedPrefs?.edit { putString(key, value.toString()) }
    }

    fun putLong(key: String, value: Long) {
        sharedPrefs?.edit { putLong(key, value) }
    }

    fun getInt(key: String): Int? {
        if (!contains(key)) {
            return null
        }
        return sharedPrefs?.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean? {
        if (!contains(key)) {
            return null
        }
        return sharedPrefs?.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        if (!contains(key)) {
            return null
        }
        return sharedPrefs?.getString(key, null)
    }

    fun getFloat(key: String): Float? {
        if (!contains(key)) {
            return null
        }
        return sharedPrefs?.getFloat(key, 0f)
    }

    fun getDouble(key: String): Double? {
        if (!contains(key)) {
            return null
        }
        return sharedPrefs?.getString(key, null)?.toDoubleCompat()
    }

    fun getLong(key: String): Long? {
        if (!contains(key)) {
            return null
        }
        return sharedPrefs?.getLong(key, 0L)
    }

    fun putCoordinate(key: String, value: Coordinate){
        putDouble(key + "_latitude", value.latitude)
        putDouble(key + "_longitude", value.longitude)
    }

    fun getCoordinate(key: String): Coordinate? {
        val latitude = getDouble(key + "_latitude") ?: return null
        val longitude = getDouble(key + "_longitude") ?: return null

        return Coordinate(latitude, longitude)
    }

    fun putInstant(key: String, value: Instant){
        putLong(key, value.toEpochMilli())
    }

    fun getInstant(key: String): Instant? {
        val time = getLong(key) ?: return null
        return Instant.ofEpochMilli(time)
    }
}