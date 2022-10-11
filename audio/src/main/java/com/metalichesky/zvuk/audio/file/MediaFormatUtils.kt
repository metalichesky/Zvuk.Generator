package com.metalichesky.zvuk.audio.file

import android.media.AudioFormat
import android.media.MediaFormat
import android.os.Build
import com.metalichesky.zvuk.audio.AudioParams
import com.metalichesky.zvuk.audio.Encoding
import java.lang.Exception

fun MediaFormat.getIntegerOrNull(key: String): Int? {
    return try {
        getInteger(key)
    } catch (ex: Exception) {
        null
    }
}

fun MediaFormat.getAudioParams(): AudioParams {
    val sampleRate = getIntegerOrNull(MediaFormat.KEY_SAMPLE_RATE) ?: 44100
    val channelsCount = getIntegerOrNull(MediaFormat.KEY_CHANNEL_COUNT) ?: 2
    val bitrate = if (Build.VERSION.SDK_INT > 23) {
        val encoding = getIntegerOrNull(MediaFormat.KEY_PCM_ENCODING)
        when(encoding) {
            AudioFormat.ENCODING_PCM_8BIT -> 8
            AudioFormat.ENCODING_PCM_16BIT -> 16
            AudioFormat.ENCODING_PCM_FLOAT -> 32
            else -> 32
        }
    } else {
        16
    }
    return AudioParams(
        channelsCount = channelsCount,
        sampleRate = sampleRate,
        encoding = when (bitrate) {
            8 -> Encoding.PCM_8BIT
            16 -> Encoding.PCM_16BIT
            24 -> Encoding.PCM_24BIT
            else -> Encoding.PCM_FLOAT
        }
    )
}