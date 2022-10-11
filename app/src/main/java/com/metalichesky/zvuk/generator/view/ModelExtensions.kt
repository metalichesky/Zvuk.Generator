package com.metalichesky.zvuk.generator.view

import com.metalichesky.zvuk.audio.WaveformType
import com.metalichesky.zvuk.generator.R
import com.metalichesky.zvuk.audio.NoiseType

fun WaveformType.getIconResId(): Int = when (this) {
    WaveformType.SINE -> R.drawable.ic_waveform_sine
    WaveformType.SAW_TOOTH -> R.drawable.ic_waveform_sawtooth
    WaveformType.SQUARE -> R.drawable.ic_waveform_square
    WaveformType.TRIANGLE -> R.drawable.ic_waveform_triangle
}

fun WaveformType.getNameResId(): Int = when (this) {
    WaveformType.SINE -> R.string.waveform_sine
    WaveformType.SAW_TOOTH -> R.string.waveform_saw_tooth
    WaveformType.SQUARE -> R.string.waveform_square
    WaveformType.TRIANGLE -> R.string.waveform_triangle
}

fun NoiseType.getNameResId(): Int = when(this) {
    NoiseType.BROWN -> R.string.noise_brown
    NoiseType.PINK -> R.string.noise_pink
    NoiseType.WHITE -> R.string.noise_white
    NoiseType.BLUE -> R.string.noise_blue
    NoiseType.VIOLET -> R.string.noise_violet
}

fun NoiseType.getColorResId(): Int = when(this) {
    NoiseType.BROWN -> R.color.brown_noise
    NoiseType.PINK -> R.color.pink_noise
    NoiseType.WHITE -> R.color.white_noise
    NoiseType.BLUE -> R.color.blue_noise
    NoiseType.VIOLET -> R.color.violet_noise
}