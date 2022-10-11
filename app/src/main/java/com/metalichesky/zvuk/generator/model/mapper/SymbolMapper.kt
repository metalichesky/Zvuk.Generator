package com.metalichesky.zvuk.generator.model.mapper

import com.metalichesky.zvuk.generator.model.Symbol
import com.metalichesky.zvuk.storage.SymbolDto

object SymbolMapper: Mapper<SymbolDto, Symbol> {

    @Throws(IllegalArgumentException::class)
    override fun map(input: SymbolDto): Symbol {
        return Symbol(
            alphabetId = input.alphabetId ?: throw IllegalArgumentException("Wrong symbol format: symbol is null"),
            symbol = input.symbol ?: throw IllegalArgumentException("Wrong symbol format: symbol is null"),
            frequency = input.frequency ?: throw IllegalArgumentException("Wrong symbol format: frequency is null"),
            morseCode = MorseCodeMapper.map(input.morseCode ?: throw IllegalArgumentException("Wrong symbol format: morseCode is null"))
        )
    }

}