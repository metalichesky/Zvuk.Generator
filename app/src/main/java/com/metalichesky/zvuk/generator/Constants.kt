package com.metalichesky.zvuk.generator

import com.metalichesky.zvuk.audio.NoiseType
import com.metalichesky.zvuk.audio.Volume
import com.metalichesky.zvuk.audio.WaveformType

object Constants {

    const val MIN_FREQUENCY = 20
    const val MAX_FREQUENCY = 8000

    const val MIN_SPEED = 5
    const val MAX_SPEED = 60

    const val MIN_TEXT_SIZE = 5
    const val MAX_TEXT_SIZE = 90
    const val DEFAULT_TEXT_SIZE = 30

    val DEFAULT_VOLUME = Volume.fromRatio(0.8f)
    val DEFAULT_NOISE_VOLUME = Volume.fromDb(DEFAULT_VOLUME.getDb() / 2f)
    const val DEFAULT_FREQUENCY = 300f
    val DEFAULT_WAVEFORM = WaveformType.TRIANGLE
    val DEFAULT_NOISE_TYPE = NoiseType.WHITE
    const val DEFAULT_GROUPS_PER_MINUTE = 12f

}