package com.kylecorry.trailsensecore.infrastructure.persistence

abstract class Dto<T> {

    protected val finalProperties: MutableMap<String, Any?> = mutableMapOf()

    abstract fun getProperties(): Map<String, SqlType>

    fun set(property: String, value: Any?) {
        finalProperties[property] = value
    }

    abstract fun toObject(): T
}