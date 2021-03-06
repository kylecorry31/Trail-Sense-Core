package com.kylecorry.trailsensecore.infrastructure.audio

import android.media.AudioTrack
import kotlin.math.sin

class ToneGenerator {

    private val soundGenerator = SoundGenerator()

    fun getTone(frequency: Int, sampleRate: Int = 64000, durationSeconds: Int = 1): AudioTrack {
        return soundGenerator.getSound(sampleRate, durationSeconds) {
            sin(frequency * 2 * Math.PI * it / sampleRate)
        }
    }

}