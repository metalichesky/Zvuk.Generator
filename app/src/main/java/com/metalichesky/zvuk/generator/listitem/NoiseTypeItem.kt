package com.metalichesky.zvuk.generator.listitem

import com.metalichesky.zvuk.audio.NoiseType

data class NoiseTypeItem(
    val noiseType: NoiseType,
    val isSelected: Boolean = false
): BaseItem