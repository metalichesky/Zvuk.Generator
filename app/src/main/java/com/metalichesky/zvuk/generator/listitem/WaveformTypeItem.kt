package com.metalichesky.zvuk.generator.listitem

import com.metalichesky.zvuk.audio.WaveformType

data class WaveformTypeItem(
    val waveformType: WaveformType,
    val isSelected: Boolean = false
): BaseItem