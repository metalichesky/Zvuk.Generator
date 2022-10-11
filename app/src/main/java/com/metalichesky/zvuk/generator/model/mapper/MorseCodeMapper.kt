package com.metalichesky.zvuk.generator.model.mapper

import com.metalichesky.zvuk.generator.model.MorseCode
import com.metalichesky.zvuk.generator.model.MorseSymbol
import com.metalichesky.zvuk.storage.MorseCodeDto

object MorseCodeMapper : Mapper<MorseCodeDto, MorseCode> {

    private val CHECK_NOTATION_REGEX = Regex("[.-]+")
    private val SLPIT_REGEX = Regex("")

    @Throws(IllegalArgumentException::class)
    override fun map(input: MorseCodeDto) : MorseCode {
        return if (input.notation.matches(CHECK_NOTATION_REGEX)) {
            val morseSymbols = input.notation.split(SLPIT_REGEX).mapNotNull {
                when (it) {
                    MorseSymbol.DASH.notation -> MorseSymbol.DASH
                    MorseSymbol.DOT.notation -> MorseSymbol.DOT
                    else -> null
                }
            }
            MorseCode(morseSymbols = morseSymbols)
        } else {
            throw IllegalArgumentException("Wrong morse code format")
            // empty or wrong string
        }

    }

}