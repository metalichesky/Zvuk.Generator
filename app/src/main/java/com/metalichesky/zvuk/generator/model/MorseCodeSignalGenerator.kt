package com.metalichesky.zvuk.generator.model

import com.metalichesky.zvuk.audio.Channel
import com.metalichesky.zvuk.audio.NoiseType
import com.metalichesky.zvuk.audio.Volume
import com.metalichesky.zvuk.audio.WaveformType
import com.metalichesky.zvuk.audio.generator.FrequencyGenerator
import com.metalichesky.zvuk.audio.generator.NoiseGenerator
import com.metalichesky.zvuk.audio.generator.SilenceGenerator
import com.metalichesky.zvuk.audio.transformer.AudioDataTransformer
import com.metalichesky.zvuk.audio.transformer.AudioMixer
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToLong
import kotlin.system.measureTimeMillis

@Singleton
class MorseCodeSignalGenerator @Inject constructor() {

    private val frequencyGenerator = FrequencyGenerator()
    private val silenceGenerator = SilenceGenerator()
    private val noiseGenerator = NoiseGenerator()
    private val channels: MutableList<Channel> = mutableListOf()

    var currentVolume: Volume = Volume.fromRatio(1f)
    var currentNoiseVolume: Volume = Volume.fromRatio(0.5f)
    var currentFrequency: Float = 300f
    var currentWaveformType: WaveformType = WaveformType.SINE
    var currentNoiseType: NoiseType = NoiseType.WHITE
    var currentGroupPerMinute = 12f
    var currentSignalPaddingMs: Long = 100L

    private var startPadding: Boolean = true
    private var endPadding: Boolean = true

    private var currentSymbolIdx: Int = 0

    private var generatorJob: Job? = null

    var listener: Listener? = null

    var paused: Boolean = false
        private set
    val active: Boolean
        get() {
            return generatorJob?.isActive ?: false
        }

    val usedAlphabets: MutableList<Alphabet> = mutableListOf()
    val symbolsStack: Queue<Symbol> = ConcurrentLinkedQueue()

    fun addAlphabet(alphabet: Alphabet) {
        if (this.usedAlphabets.find { it.id == alphabet.id } != null) {
            return
        }
        usedAlphabets.add(alphabet)
    }

    private fun getAlphabet(alphabetId: Long): Alphabet? {
        return usedAlphabets.find { it.id == alphabetId }
    }

    fun setText(text: SymbolsText) {
        symbolsStack.clear()
        addText(text)
    }

    fun addText(text: SymbolsText) {
        text.symbols.forEachIndexed { _, symbol ->
            symbolsStack.offer(symbol)
        }
    }

    fun clear() {
        symbolsStack.clear()
    }

    fun start(coroutineScope: CoroutineScope) {
        resume()
        startPadding = true
        endPadding = true
        generatorJob = coroutineScope.launch(Dispatchers.Default) {
            listener?.onStarted()
//            Log.d(LOG_TAG, "start generator")
            while (isActive) {
//                Log.d(LOG_TAG, "generator isActive symbols ${symbolsStack.size}")
                val symbol = symbolsStack.poll() ?: break
                val alphabet = getAlphabet(symbol.alphabetId) ?: continue
                val unit = alphabet.getUnitMs(currentGroupPerMinute)
                val soundSequence = symbol.morseCode.getSoundSequence()
                val dataTransformer = AudioDataTransformer()

                soundSequence.add(MorsePause.BETWEEN_SYMBOLS)

//                val sequence =
//                    soundSequence.joinToString { "beep = ${it.silence} pause ${it.unitDuration}" }
//                Log.d(LOG_TAG, "symbol ${symbol} sequence ${sequence}")

                if (paused) {
                    listener?.onPaused()
                    while (paused && isActive) { /*stuck in loop to pause*/
                        delay(10)
//                    Log.d(LOG_TAG, "paused")
                    }
                    if (isActive) {
                        listener?.onResumed()
                    }
                }

                if (startPadding) {
                    startPadding = false
                    val paddingBuffer = dataTransformer.generateFloatArray(currentSignalPaddingMs.toFloat())

                    if (!currentNoiseVolume.isMin()) {
                        noiseGenerator.generateNoise(
                            paddingBuffer,
                            currentNoiseType,
                            currentNoiseVolume.getRatio()
                        )
                    } else {
                        silenceGenerator.generate(
                            paddingBuffer
                        )
                    }

                    sendDataToAllChannels(dataTransformer.floatArrayToByteArray(paddingBuffer))
                }

                soundSequence.forEachIndexed { _, sound ->
                    val duration = (sound.unitDuration * unit).roundToLong()

                    val generatorTake = measureTimeMillis {
                        val audioBuffer = dataTransformer.generateFloatArray(duration.toFloat())
                        val noiseBuffer = dataTransformer.generateFloatArray(duration.toFloat())
                        if (!sound.silence) {
                            frequencyGenerator.generate(
                                audioBuffer,
                                currentWaveformType,
                                currentFrequency,
                                currentVolume.getRatio()
                            )
                        } else {
                            silenceGenerator.generate(audioBuffer)
                        }
                        // generate noise and mix to audio is noise volume isn't minimal
                        if (!currentNoiseVolume.isMin()) {
                            noiseGenerator.generateNoise(
                                noiseBuffer,
                                currentNoiseType,
                                currentNoiseVolume.getRatio()
                            )
                            AudioMixer.mix(audioBuffer, noiseBuffer, audioBuffer)
                        }

                        sendDataToAllChannels(dataTransformer.floatArrayToByteArray(audioBuffer))
                    }
                    val delayDuration = duration - generatorTake + DELAY_FIX
                    if (delayDuration > 0) {
                        delay(delayDuration)
                    }
                }

                if (endPadding) {
                    endPadding = false
                    val paddingBuffer = dataTransformer.generateFloatArray(currentSignalPaddingMs.toFloat())
                    if (!currentNoiseVolume.isMin()) {
                        noiseGenerator.generateNoise(
                            paddingBuffer,
                            currentNoiseType,
                            currentNoiseVolume.getRatio()
                        )
                    } else {
                        silenceGenerator.generate(
                            paddingBuffer
                        )
                    }
                    sendDataToAllChannels(dataTransformer.floatArrayToByteArray(paddingBuffer))
                }
            }
            if (!isActive) {
                listener?.onStopped()
            }
            listener?.onCompleted()
//            Log.d(LOG_TAG, "end generator")
        }
        generatorJob?.start()
    }

    private suspend fun sendDataToAllChannels(byteArray: ByteArray) {
        channels.forEach {
//            Log.d(LOG_TAG, "sendDataToAllChannels: byteArray ${byteArray.size}")
            it.sendData(byteArray)
        }
    }

    fun pause() {
        paused = true
    }

    fun resume() {
        paused = false
    }

    fun stop() {
        generatorJob?.cancel()
    }

    fun addReceiver(): Channel {
        val newChannel = Channel()
        channels.add(newChannel)
        return newChannel
    }

    interface Listener {

        fun onStarted()

        fun onStopped()

        fun onPaused()

        fun onResumed()

        fun onCompleted()

    }

}

private const val DELAY_FIX = -1L
private const val LOG_TAG = "MorseCodeGenerator"