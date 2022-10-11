package com.metalichesky.zvuk.generator.model

import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

class Alphabet(
    val id: Long,
    val symbols: List<Symbol>,
    val type: AlphabetType
) {

    private val rand = Random(System.currentTimeMillis())

    fun findSymbol(string: String): Symbol? {
        return symbols.find {
            println("${it} equals ${string}")
            it.symbol == string
        }
    }

    fun getAvgSymbolUnits(): Float {
        var avgSymbolUnits = 0f
        symbols.forEach {
            val symbolPausesUnits = MorsePause.IN_SYMBOL.unitDuration * (it.morseCode.morseSymbols.size - 1).toFloat()
            val symbolUnits = it.morseCode.morseSymbols.sumOf { symbol -> symbol.unitDuration.toDouble() }.toFloat()
            avgSymbolUnits += (symbolPausesUnits + symbolUnits) * it.frequency
        }
        return avgSymbolUnits
    }

    fun getUnitMs(groupPerMinute: Float): Float {


        val groupsCount = groupPerMinute.roundToInt()
        val pausesBetweenGroups = max(groupsCount - 1f, 0f)
        val pausesBetweenGroupsUnits = MorsePause.BETWEEN_GROUPS.unitDuration * pausesBetweenGroups

        val groupSize = MorseCode.MORSE_GROUP_SIZE

        val pausesBetweenSymbols = (groupSize - 1) * groupPerMinute
        val pausesBetweenSymbolsUnits = MorsePause.BETWEEN_SYMBOLS.unitDuration * pausesBetweenSymbols

        val pausesUnits = pausesBetweenGroupsUnits + pausesBetweenSymbolsUnits

        val symbolsCount = groupPerMinute * groupSize
        val symbolUnits = getAvgSymbolUnits() * symbolsCount

        val allUnits  = pausesUnits + symbolUnits
        val unitDuration = MINUTE_MS.toFloat() / allUnits
        return unitDuration
    }

    fun getRandSymbol(): Symbol {
        var qumulative = 0f

        val max = symbols.sumOf { it.frequency.toDouble() }.toFloat()
        val randFloat = rand.nextFloat() * max
        var randIndex = 0

        var previousCumulative: Float
        for (i in symbols.indices) {
            previousCumulative = qumulative
            qumulative += symbols[i].frequency
            if (randFloat in previousCumulative..qumulative) {
                randIndex = i
                break
            }
        }

        return symbols[randIndex]
    }
}

private const val MINUTE_MS = 60_000