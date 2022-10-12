package com.metalichesky.zvuk.audio

import com.metalichesky.zvuk.audio.math.equalsAlmost
import kotlin.math.log
import kotlin.math.pow

class Volume {
    companion object {
        const val VOLUME_MIN_RATIO = 0.001f
        const val VOLUME_MAX_RATIO = 1f
        val VOLUME_MAX_DB = fromRatio(VOLUME_MAX_RATIO).getDb()

        fun fromDb(db: Float): Volume {
            return Volume().apply {
                val ratio = 10f.pow(db / 10f) * VOLUME_MIN_RATIO
                this.internal = ratio.coerceIn(0f, VOLUME_MAX_RATIO)
            }
        }

        fun fromRatio(ratio: Float): Volume {
            return Volume().apply {
                this.internal = ratio.coerceIn(0f, VOLUME_MAX_RATIO)
            }
        }
    }

    private var internal = 0f

    private constructor()

    fun getDb(): Float {
        return log(internal / VOLUME_MIN_RATIO, 10f) * 10f
    }

    fun getRatio(): Float {
        return internal
    }

    fun isMin(): Boolean {
        return internal.equalsAlmost(VOLUME_MIN_RATIO, Float.MIN_VALUE)
    }

    fun isMax(): Boolean {
        return internal.equalsAlmost(VOLUME_MAX_RATIO, Float.MIN_VALUE)
    }

}

fun Float.asVolumeDb(): Volume = Volume.fromDb(this)

fun Float.asVolumeRatio(): Volume = Volume.fromRatio(this)