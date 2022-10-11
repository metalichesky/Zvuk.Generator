package com.metalichesky.zvuk.generator.model

abstract class MorseSound(val unitDuration: Float, val silence: Boolean) {}

class MorseSymbol(val notation: String, unitDuration: Float) : MorseSound(unitDuration, false) {
    companion object {
        val DOT = MorseSymbol(".", 1f)
        val DASH = MorseSymbol("-", 3f)
    }
}

class MorsePause(unitDuration: Float) : MorseSound(unitDuration, true) {
    companion object {
        val IN_SYMBOL = MorsePause(1f)
        val BETWEEN_SYMBOLS = MorsePause(3f)
        val BETWEEN_GROUPS = MorsePause(7f)
    }
}

class MorseCode(
    val morseSymbols: List<MorseSymbol>
) {


    fun getSoundSequence(): MutableList<MorseSound> {
        val soundSequence = mutableListOf<MorseSound>()
        for (i in morseSymbols.indices) {
            soundSequence.add(morseSymbols[i])
            if (i < morseSymbols.size - 1) {
                soundSequence.add(MorsePause.IN_SYMBOL)
            }
        }
        return soundSequence
    }

    override fun toString(): String {
        return morseSymbols.map { it.notation }.joinToString("")
    }

    companion object {

        /**
         * Default Morse code word length
         */
        const val MORSE_GROUP_SIZE = 5

    }

}