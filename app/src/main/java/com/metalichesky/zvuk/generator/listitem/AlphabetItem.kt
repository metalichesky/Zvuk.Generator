package com.metalichesky.zvuk.generator.listitem

import com.metalichesky.zvuk.generator.model.Alphabet

data class AlphabetItem(
    val alphabet: Alphabet,
    val isSelected: Boolean
): BaseItem