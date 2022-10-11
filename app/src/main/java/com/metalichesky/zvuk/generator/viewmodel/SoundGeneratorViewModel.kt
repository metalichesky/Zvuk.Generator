package com.metalichesky.zvuk.generator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalichesky.zvuk.audio.NoiseType
import com.metalichesky.zvuk.audio.Volume
import com.metalichesky.zvuk.audio.WaveformType
import com.metalichesky.zvuk.generator.listitem.AlphabetItem
import com.metalichesky.zvuk.generator.listitem.NoiseTypeItem
import com.metalichesky.zvuk.generator.listitem.WaveformTypeItem
import com.metalichesky.zvuk.generator.model.Alphabet
import com.metalichesky.zvuk.generator.model.AlphabetInteractor
import com.metalichesky.zvuk.generator.model.MorseGeneratorInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SoundGeneratorViewModel @Inject constructor(
    private val generatorInteractor: MorseGeneratorInteractor,
    private val alphabetInteractor: AlphabetInteractor
) : ViewModel() {
    companion object {
        const val LOG_TAG = "SoundGeneratorViewModel"
    }

    val playing = generatorInteractor.playingFlow

    val volume = generatorInteractor.volumeFlow

    val noiseVolume = generatorInteractor.noiseVolumeFlow

    val frequency = generatorInteractor.frequencyFlow

    val groupsPerMinute = generatorInteractor.groupsPerMinuteFlow

    var textSize = generatorInteractor.textSizeFlow

    val waveformTypeItemsFlow: Flow<List<WaveformTypeItem>> = generatorInteractor.waveformTypeFlow.map { currentWaveformType ->
        WaveformType.values().toList().map { waveformType ->
            WaveformTypeItem(
                waveformType = waveformType,
                isSelected = waveformType.id == currentWaveformType.id
            )
        }
    }

    val noiseTypeItemsFlow: Flow<List<NoiseTypeItem>> = generatorInteractor.noiseTypeFlow.map { currentNoiseType ->
        NoiseType.values().toList().map { noiseType ->
            NoiseTypeItem(
                noiseType = noiseType,
                isSelected = noiseType.id == currentNoiseType.id
            )
        }
    }

    val alphabetFlow = alphabetInteractor.defaultAlphabet.map { defaultAlphabet ->
        alphabetInteractor.getAllAlphabets().map { alphabet ->
            AlphabetItem(
                alphabet = alphabet,
                isSelected = alphabet.id == defaultAlphabet?.id
            )
        }
    }

    fun start() {
        generatorInteractor.start()
    }

    fun play() {
        generatorInteractor.play()
    }

    fun pause() {
        generatorInteractor.pause()
    }

    fun stop() {
        generatorInteractor.stop()
    }

    fun setWaveformType(type: WaveformType) {
        generatorInteractor.setWaveformType(type)
    }

    fun setNoiseType(type: NoiseType) {
        generatorInteractor.setNoiseType(type)
    }

    fun setFrequency(newFrequency: Float) {
        generatorInteractor.setFrequency(newFrequency)
    }

    fun getFrequency(): Float {
        return generatorInteractor.getFrequency()
    }

    fun setVolume(newVolume: Volume) {
        generatorInteractor.setVolume(newVolume)
    }

    fun getVolume(): Volume {
        return generatorInteractor.getVolume()
    }

    fun setNoiseVolume(newVolume: Volume) {
        generatorInteractor.setNoiseVolume(newVolume)
    }

    fun getNoiseVolume(): Volume {
        return generatorInteractor.getNoiseVolume()
    }

    fun setGroupsPerMinute(groupsPerMinute: Float) {
        generatorInteractor.setGroupsPerMinute(groupsPerMinute)
    }

    fun getGroupsPerMinute(): Float {
        return generatorInteractor.getGroupsPerMinute()
    }

    fun setTextSize(textSize: Int) {
        generatorInteractor.setTextSize(textSize)
    }

    fun getTextSize(): Int {
        return generatorInteractor.getTextSize()
    }

    fun setAlphabet(alphabet: Alphabet) {
        viewModelScope.launch (Dispatchers.IO) {
            alphabetInteractor.setDefaultAlphabet(alphabet.id)
        }
    }

    override fun onCleared() {
        stop()
        super.onCleared()
    }

}