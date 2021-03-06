package com.kylecorry.trailsensecore.infrastructure.sensors

import com.kylecorry.trailsensecore.domain.units.Quality

abstract class AbstractSensor: ISensor {

    override val quality = Quality.Unknown

    private val listeners = mutableSetOf<SensorListener>()
    private var started = false

    override fun start(listener: SensorListener){
        synchronized(listeners) {
            listeners.add(listener)
        }
        if (started) return
        startImpl()
        started = true
    }

    override fun stop(listener: SensorListener?){
        synchronized(listeners) {
            if (listener != null) {
                listeners.remove(listener)
            } else {
                listeners.clear()
            }
        }
        if (listeners.isNotEmpty()) return
        if (!started) return
        stopImpl()
        started = false
    }

    protected abstract fun startImpl()
    protected abstract fun stopImpl()

    protected fun notifyListeners(){
        synchronized(listeners) {
            val finishedListeners = listeners.filter { !it.invoke() }
            finishedListeners.forEach(::stop)
        }
    }

}