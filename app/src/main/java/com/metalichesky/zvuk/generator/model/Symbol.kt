package com.metalichesky.zvuk.generator.model

class Symbol(
    val alphabetId: Long,
    val symbol: String,
    var frequency: Float,
    var morseCode: MorseCode
) {
    override fun toString(): String {
        return symbol
    }
}