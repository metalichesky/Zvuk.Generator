package com.metalichesky.zvuk.generator.model

import com.metalichesky.zvuk.audio.NoiseType
import com.metalichesky.zvuk.audio.Volume
import com.metalichesky.zvuk.audio.WaveformType
import com.metalichesky.zvuk.audio.player.SamplesPlayer
import com.metalichesky.zvuk.generator.Constants
import com.metalichesky.zvuk.audio.Channel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MorseGeneratorInteractor @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val alphabetRepo: AlphabetRepo,
    private val generator: MorseCodeSignalGenerator
) {

    private val samplesPlayer: SamplesPlayer = SamplesPlayer()

    private val generatorListener = object: MorseCodeSignalGenerator.Listener {

        override fun onStarted() {

        }

        override fun onPaused() {

        }

        override fun onResumed() {

        }

        override fun onStopped() {

        }

        override fun onCompleted() {
            this@MorseGeneratorInteractor.onCompleted()
        }

    }

    var job: Job? = null
    var channel: Channel? = null

    private val playing = MutableStateFlow<Boolean>(false)
    val playingFlow: StateFlow<Boolean> = playing.asStateFlow()

    private val volume = MutableStateFlow(Constants.DEFAULT_VOLUME)
    val volumeFlow: StateFlow<Volume> = volume.asStateFlow()

    private val noiseVolume = MutableStateFlow<Volume>(Constants.DEFAULT_NOISE_VOLUME)
    val noiseVolumeFlow: StateFlow<Volume> = noiseVolume.asStateFlow()

    private val frequency = MutableStateFlow<Float>(Constants.DEFAULT_FREQUENCY)
    val frequencyFlow: StateFlow<Float> = frequency.asStateFlow()

    private val waveformType = MutableStateFlow<WaveformType>(Constants.DEFAULT_WAVEFORM)
    val waveformTypeFlow: StateFlow<WaveformType> = waveformType.asStateFlow()

    private val noiseType = MutableStateFlow<NoiseType>(Constants.DEFAULT_NOISE_TYPE)
    val noiseTypeFlow: StateFlow<NoiseType> = noiseType.asStateFlow()

    private val groupsPerMinute = MutableStateFlow<Float>(Constants.DEFAULT_GROUPS_PER_MINUTE)
    val groupsPerMinuteFlow: StateFlow<Float> = groupsPerMinute.asStateFlow()

    private val textSize = MutableStateFlow<Int>(Constants.DEFAULT_TEXT_SIZE)
    val textSizeFlow: StateFlow<Int> = textSize.asStateFlow()

    init {
        generator.listener = generatorListener
    }

    fun start() {
        stop()
        samplesPlayer.start()
    }

    fun play() {
        coroutineScope.launch(Dispatchers.Default) {
            val alphabet = alphabetRepo.getDefaultAlphabet() ?: return@launch
            val text = SymbolsText()
            text.addAlphabet(alphabet)
            text.generateRandom(textSize.value * MorseCode.MORSE_GROUP_SIZE)

            generator.clear()
            generator.addAlphabet(alphabet)
            generator.addText(text)

            updateGenerator()

            channel = generator.addReceiver()

            generator.start(coroutineScope)

            job = coroutineScope.launch(Dispatchers.IO) {
                while (job?.isActive == true) {
                    val data = channel?.receiveData()
                    if (data != null) {
                        samplesPlayer.play(data)
                    }
                }
                pause()
            }

            withContext(Dispatchers.Main) {
                playing.value = true
            }
        }
    }

    fun pause() {
        coroutineScope.launch(Dispatchers.Default) {
            job?.cancel()
            generator.stop()
            withContext(Dispatchers.Main) {
                playing.value = false
            }
        }
    }

    fun stop() {
        pause()
        samplesPlayer.stop()
    }

    private fun onCompleted() {
        coroutineScope.launch(Dispatchers.Default) {
            job?.cancel()
            withContext(Dispatchers.Main) {
                playing.value = false
            }
        }
    }

    private fun updateGenerator() {
        generator.currentFrequency = frequency.value
        generator.currentVolume = volume.value
        generator.currentNoiseVolume = noiseVolume.value
        generator.currentWaveformType = waveformType.value
        generator.currentNoiseType = noiseType.value
        generator.currentGroupPerMinute = groupsPerMinute.value
    }


    fun setWaveformType(type: WaveformType) {
        waveformType.value = type
        updateGenerator()
    }

    fun setNoiseType(type: NoiseType) {
        noiseType.value = type
        updateGenerator()
    }

    fun setFrequency(newFrequency: Float) {
        frequency.value= newFrequency
        updateGenerator()
    }

    fun getFrequency(): Float {
        return frequency.value
    }

    fun setVolume(newVolume: Volume) {
        volume.value = newVolume
        updateGenerator()
    }

    fun getVolume(): Volume {
        return volume.value
    }

    fun setNoiseVolume(newVolume: Volume) {
        noiseVolume.value = newVolume
        updateGenerator()
    }

    fun getNoiseVolume(): Volume {
        return noiseVolume.value
    }

    fun setGroupsPerMinute(groupsPerMinute: Float) {
        this.groupsPerMinute.value = groupsPerMinute
        updateGenerator()
    }

    fun getGroupsPerMinute(): Float {
        return groupsPerMinute.value
    }

    fun setTextSize(textSize: Int) {
        this.textSize.value = textSize
        updateGenerator()
    }

    fun getTextSize(): Int {
        return this.textSize.value
    }


}