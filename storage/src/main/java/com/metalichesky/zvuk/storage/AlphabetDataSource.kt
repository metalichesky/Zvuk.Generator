package com.metalichesky.zvuk.storage

class AlphabetDataSource {

    private val englishAlphabet: AlphabetDto = AlphabetDto(
        ENGLISH_ALPHABET_ID,
        listOf(
            SymbolDto(ENGLISH_ALPHABET_ID, "A", 0.082f, MorseCodeDto(".-")),
            SymbolDto(ENGLISH_ALPHABET_ID, "B", 0.015f, MorseCodeDto("-...")),
            SymbolDto(ENGLISH_ALPHABET_ID, "C", 0.028f, MorseCodeDto("-.-.")),
            SymbolDto(ENGLISH_ALPHABET_ID, "D", 0.043f, MorseCodeDto("-..")),
            SymbolDto(ENGLISH_ALPHABET_ID, "E", 0.13f, MorseCodeDto(".")),
            SymbolDto(ENGLISH_ALPHABET_ID, "F", 0.020f, MorseCodeDto("..-.")),
            SymbolDto(ENGLISH_ALPHABET_ID, "G", 0.02f, MorseCodeDto("--.")),
            SymbolDto(ENGLISH_ALPHABET_ID, "H", 0.061f, MorseCodeDto("....")),
            SymbolDto(ENGLISH_ALPHABET_ID, "I", 0.07f, MorseCodeDto("..")),
            SymbolDto(ENGLISH_ALPHABET_ID, "J", 0.0015f, MorseCodeDto(".---")),
            SymbolDto(ENGLISH_ALPHABET_ID, "K", 0.0075f, MorseCodeDto("-.-")),
            SymbolDto(ENGLISH_ALPHABET_ID, "L", 0.04f, MorseCodeDto(".-..")),
            SymbolDto(ENGLISH_ALPHABET_ID, "M", 0.024f, MorseCodeDto("--")),
            SymbolDto(ENGLISH_ALPHABET_ID, "N", 0.067f, MorseCodeDto("-.")),
            SymbolDto(ENGLISH_ALPHABET_ID, "O", 0.075f, MorseCodeDto("---")),
            SymbolDto(ENGLISH_ALPHABET_ID, "P", 0.019f, MorseCodeDto(".--.")),
            SymbolDto(ENGLISH_ALPHABET_ID, "Q", 0.00095f, MorseCodeDto("--.-")),
            SymbolDto(ENGLISH_ALPHABET_ID, "R", 0.06f, MorseCodeDto(".-.")),
            SymbolDto(ENGLISH_ALPHABET_ID, "S", 0.061f, MorseCodeDto("...")),
            SymbolDto(ENGLISH_ALPHABET_ID, "T", 0.091f, MorseCodeDto("-")),
            SymbolDto(ENGLISH_ALPHABET_ID, "U", 0.028f, MorseCodeDto("..-")),
            SymbolDto(ENGLISH_ALPHABET_ID, "V", 0.0098f, MorseCodeDto("...-")),
            SymbolDto(ENGLISH_ALPHABET_ID, "W", 0.024f, MorseCodeDto(".--")),
            SymbolDto(ENGLISH_ALPHABET_ID, "X", 0.0015f, MorseCodeDto("-..-")),
            SymbolDto(ENGLISH_ALPHABET_ID, "Y", 0.02f, MorseCodeDto("-.--")),
            SymbolDto(ENGLISH_ALPHABET_ID, "Z", 0.00074f, MorseCodeDto("--.."))
        ),
        "ENGLISH"
    )

    /**
     * Symbols 'Ъ' and 'Ё' are not used in morse code
     */
    private val russianAlphabet: AlphabetDto = AlphabetDto(
        RUSSIAN_ALPHABET_ID,
        listOf(
            SymbolDto(RUSSIAN_ALPHABET_ID, "А", 0.0801f, MorseCodeDto(".-")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Б", 0.0159f, MorseCodeDto("-...")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "В", 0.0454f, MorseCodeDto(".--")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Г", 0.017f, MorseCodeDto("--.")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Д", 0.0298f, MorseCodeDto("-..")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Е", 0.0845f, MorseCodeDto(".")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ж", 0.0094f, MorseCodeDto("...-")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "З", 0.0165f, MorseCodeDto("--..")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "И", 0.0735f, MorseCodeDto("..")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Й", 0.0121f, MorseCodeDto(".---")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "К", 0.0349f, MorseCodeDto("-.-")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Л", 0.044f, MorseCodeDto(".-..")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "М", 0.0321f, MorseCodeDto("--")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Н", 0.067f, MorseCodeDto("-.")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "О", 0.1097f, MorseCodeDto("---")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "П", 0.0281f, MorseCodeDto(".--.")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Р", 0.0473f, MorseCodeDto(".-.")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "С", 0.0546f, MorseCodeDto("...")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Т", 0.0626f, MorseCodeDto("-")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "У", 0.0262f, MorseCodeDto("..-")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ф", 0.0026f, MorseCodeDto("..-.")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Х", 0.0097f, MorseCodeDto("....")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ц", 0.0048f, MorseCodeDto("-.-.")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ч", 0.0144f, MorseCodeDto("---.")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ш", 0.0073f, MorseCodeDto("----")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Щ", 0.0036f, MorseCodeDto("--.-")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ы", 0.019f, MorseCodeDto("-.--")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ь", 0.0174f, MorseCodeDto("-..-")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Э", 0.0032f, MorseCodeDto("..-..")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Ю", 0.0064f, MorseCodeDto("..--")),
            SymbolDto(RUSSIAN_ALPHABET_ID, "Я", 0.0201f, MorseCodeDto(".-.-"))
        ),
        "RUSSIAN"
    )

    private val digitsAlphabet: AlphabetDto = AlphabetDto(
        DIGITS_ALPHABET_ID,
        listOf(
            SymbolDto(DIGITS_ALPHABET_ID, "1", 0.1f, MorseCodeDto(".----")),
            SymbolDto(DIGITS_ALPHABET_ID, "2", 0.1f, MorseCodeDto("..---")),
            SymbolDto(DIGITS_ALPHABET_ID, "3", 0.1f, MorseCodeDto("...--")),
            SymbolDto(DIGITS_ALPHABET_ID, "4", 0.1f, MorseCodeDto("....-")),
            SymbolDto(DIGITS_ALPHABET_ID, "5", 0.1f, MorseCodeDto(".....")),
            SymbolDto(DIGITS_ALPHABET_ID, "6", 0.1f, MorseCodeDto("-....")),
            SymbolDto(DIGITS_ALPHABET_ID, "7", 0.1f, MorseCodeDto("--...")),
            SymbolDto(DIGITS_ALPHABET_ID, "8", 0.1f, MorseCodeDto("---..")),
            SymbolDto(DIGITS_ALPHABET_ID, "9", 0.1f, MorseCodeDto("----.")),
            SymbolDto(DIGITS_ALPHABET_ID, "0", 0.1f, MorseCodeDto("-----"))
        ),
        "DIGITS"
    )

    private val allAlphabets = listOf(
        englishAlphabet,
        russianAlphabet,
        digitsAlphabet
    )

    suspend fun getAlphabetWithType(type: String): AlphabetDto? {
        return allAlphabets.find { it.type == type }
    }

    suspend fun getAlphabetWithId(id: Long): AlphabetDto? {
        return allAlphabets.find { it.id == id }
    }

    suspend fun getAllAlphabets(): List<AlphabetDto> {
        return allAlphabets
    }


}

internal const val ENGLISH_ALPHABET_ID = 1L
internal const val RUSSIAN_ALPHABET_ID = 2L
internal const val DIGITS_ALPHABET_ID = 3L