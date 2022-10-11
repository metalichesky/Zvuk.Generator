package com.metalichesky.zvuk.generator.model.mapper

import com.metalichesky.zvuk.generator.model.Alphabet
import com.metalichesky.zvuk.generator.model.AlphabetType
import com.metalichesky.zvuk.storage.AlphabetDto

object AlphabetMapper : Mapper<AlphabetDto, Alphabet> {

    @Throws(IllegalStateException::class)
    override fun map(input: AlphabetDto): Alphabet {
        return Alphabet(
            id = input.id ?: throw throw wrongFormatException(Alphabet::class, Alphabet::id),
            symbols = SymbolMapper.map(input.symbols ?: throw wrongFormatException(Alphabet::class, Alphabet::symbols)),
            type = input.type?.let(AlphabetType::valueOf) ?: throw wrongFormatException(Alphabet::class, Alphabet::type)
        )
    }

}