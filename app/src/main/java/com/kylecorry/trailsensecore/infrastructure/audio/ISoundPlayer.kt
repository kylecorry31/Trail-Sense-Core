package com.kylecorry.trailsensecore.infrastructure.audio

import com.kylecorry.trailsensecore.infrastructure.morse.ISignalingDevice

interface ISoundPlayer: ISignalingDevice {
    fun isOn(): Boolean
    fun fadeOn()
    fun fadeOff(releaseWhenOff: Boolean)
    fun setVolume(volume: Float)
    fun release()
}