package com.metalichesky.zvuk.audio.generator

class SilenceGenerator {

    fun generate(dataArray: FloatArray) {
        for (idx in dataArray.indices) {
            dataArray[idx] = 0f
        }
    }
}