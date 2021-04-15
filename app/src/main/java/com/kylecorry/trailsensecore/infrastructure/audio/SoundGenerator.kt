package com.kylecorry.trailsensecore.infrastructure.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

class SoundGenerator {

    fun getSound(sampleRate: Int = 64000, durationSeconds: Int = 1, sampleGenerator: (i: Int) -> Double): AudioTrack {
        // Adapted from https://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android
        val size = durationSeconds * sampleRate
        val sound = ByteArray(2 * size)

        for (i in 0 until size) {
            val sample = sampleGenerator(i)
            val pcmSound = (sample * 32767).toInt()
            sound[i * 2] = (pcmSound and 0x00ff).toByte()
            sound[i * 2 + 1] = ((pcmSound and 0xff00) shr 8).toByte()
        }

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(sound.size)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(sound, 0, sound.size)
        track.setLoopPoints(0, size, Int.MAX_VALUE)
        return track
    }

}