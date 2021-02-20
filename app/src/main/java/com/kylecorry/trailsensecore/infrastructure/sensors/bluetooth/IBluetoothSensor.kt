package com.kylecorry.trailsensecore.infrastructure.sensors.bluetooth

interface IBluetoothSensor {
    val messages: List<BluetoothMessage>
    val isConnected: Boolean

    // TODO: Make this a suspend function
    fun write(data: String): Boolean
}