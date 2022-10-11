package com.metalichesky.zvuk.storage

import com.metalichesky.zvuk.storage.MorseCodeDto

class SymbolDto(
    val alphabetId: Long?,
    val symbol: String?,
    var frequency: Float?,
    var morseCode: MorseCodeDto?
)