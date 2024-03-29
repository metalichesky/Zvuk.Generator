package com.metalichesky.zvuk.audio.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.audiofx.AudioEffect
import android.media.audiofx.BassBoost
import android.util.Log
import com.metalichesky.zvuk.audio.AudioParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.system.measureTimeMillis


class SamplesPlayer(
    private val audioSessionManager: AudioSessionManager
) {
    companion object {
        private const val LOG_TAG = "SamplesPlayer"
    }

    private var audioPlayer: AudioTrack? = null
    private var audioEffect: AudioEffect? = null
    private var mThread: Thread? = null

    private var byteData: ByteBuffer? = null

    private var isPlay = false
    private var pause = false

    private var audioParams: AudioParams = AudioParams.createDefault()
    private var playbackListener: PlaybackListener? = null


    private val bufferSize
        get() = audioParams.bytesPerMs * 2

    fun setAudioParams(audioParams: AudioParams) {
        this.audioParams = audioParams
    }

    fun setListener(listener: PlaybackListener): SamplesPlayer {
        playbackListener = listener
        return this
    }

    fun start() {
        if (isPlay && !pause) {
            stop()
        }
        if (!isPlay) {
            audioPlayer = createAudioPlayer()
        }
        isPlay = true
        pause = false
        if (audioPlayer == null) {
            Log.e(LOG_TAG, "Player can't be initialized")
            return
        }
        audioPlayer?.play()
    }

    fun getBinBufferSize(): Int {
        val channels = when (audioParams.channelsCount) {
            1 -> {
                AudioFormat.CHANNEL_OUT_MONO
            }
            2 -> {
                AudioFormat.CHANNEL_OUT_STEREO
            }
            else -> {
                AudioFormat.CHANNEL_OUT_QUAD
            }
        }

        val bufferSize = AudioTrack.getMinBufferSize(
            audioParams.sampleRate, channels, audioParams.encoding.androidType
        )
        return bufferSize
    }

    private fun getChannelsMask(channelsCount: Int): Int {
        return when (channelsCount) {
            1 -> {
                AudioFormat.CHANNEL_OUT_MONO
            }
            2 -> {
                AudioFormat.CHANNEL_OUT_FRONT_LEFT or AudioFormat.CHANNEL_OUT_FRONT_RIGHT
            }
            else -> {
                AudioFormat.CHANNEL_OUT_FRONT_LEFT or AudioFormat.CHANNEL_OUT_FRONT_RIGHT or AudioFormat.CHANNEL_OUT_BACK_LEFT or AudioFormat.CHANNEL_OUT_BACK_RIGHT
            }
        }
    }

    private fun createAudioPlayer(): AudioTrack? {
        val bufferSize = getBinBufferSize()
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        val encodingType = audioParams.encoding.androidType
        val format = AudioFormat.Builder()
            .setEncoding(encodingType)
            .setSampleRate(audioParams.sampleRate)
            .setChannelMask(getChannelsMask(audioParams.channelsCount))
            .build()
        val audioSessionId = audioSessionManager.generateAudioSessionId()
        Log.d("AudioTrack", "audio session id $audioSessionId")
        val audioTrack = try {
            AudioTrack(
                audioAttributes,
                format,
                bufferSize,
                AudioTrack.MODE_STREAM,
                audioSessionId
            )
        } catch (t: Throwable) {
            Log.e("AudioTrack", "audio track is not initialised ", t)
            null

        }
        audioTrack ?: return null

        audioEffect = try {
            BassBoost(0, audioSessionId).also {
                it.setStrength(1000)
            }
        } catch (t: Throwable) {
            null
        }

        audioEffect?.setEnableStatusListener { _, isEnabled ->
            Log.d(LOG_TAG, "audio effect enabled $isEnabled")
        }
        audioEffect?.setControlStatusListener { audioEffect, isControl ->
            Log.d(LOG_TAG, "audio effect control $isControl")
        }
        audioEffect?.enabled = true
        Log.d(LOG_TAG, "audio effect enable ${audioEffect?.enabled}")
        this.audioEffect = audioEffect

        byteData = ByteBuffer.allocate(bufferSize * 2)
        Log.d(
            LOG_TAG,
            "Bytes per ms = ${audioParams.bytesPerMs} buffer size = ${byteData?.limit()}"
        )
        return audioTrack
    }

    fun fillBuffer(byteArray: ByteArray) {

    }

    fun play(byteBuffer: ByteBuffer) {
        audioPlayer?.write(byteBuffer, 0, byteBuffer.position())
    }

    private fun addToBuffer(byteArray: ByteArray) {
        var writed = byteData?.position()
//            byteData?.put()
    }


//    fun play(byteArray: ByteArray) {
//        try {
//            val dataTime = byteArray.size / audioParams.bytesPerMs
//            val time = measureTimeMillis {
//                audioPlayer?.write(byteArray, 0, byteArray.size)
//            }
//            val sleep = dataTime - time
//            if (sleep > 0) Thread.sleep(sleep)
//            Log.d(LOG_TAG, "write time ${time} data time ${dataTime}")
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }

    fun play(byteArray: ByteArray) {
        try {
            val dataTime = byteArray.size / audioParams.bytesPerMs
            val time = measureTimeMillis {
                audioPlayer?.write(byteArray, 0, byteArray.size)
            }
//            Log.d(LOG_TAG, "write time ${time} data time ${dataTime}")
//            val sleepTime = dataTime - time
//            if (sleepTime > 0) {
//                Thread.sleep(sleepTime)
//            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createPlaybackCoroutine(coroutineScope: CoroutineScope): Job {
        return coroutineScope.launch {
            while (isActive) {
                while (pause && isActive) { // in pause }
                    if (!isActive) break
                    byteData?.clear()
                    val data = byteData?.array()
//                    Log.d("PlayerProcess", "run() bytes ${byteData} data ${data}")
                    if (data != null) {
                        try {
                            audioPlayer?.write(data, 0, data.size)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                try {
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (audioPlayer != null) {
                    if (audioPlayer!!.state != AudioTrack.PLAYSTATE_STOPPED) {
                        audioPlayer?.stop()
                        audioPlayer?.release()
                        mThread = null
                    }
                }
            }
        }
    }

    private inner class PlayerProcess : Runnable {
        override fun run() {
            while (isPlay) {
                while (pause && isPlay) {
                    // in pause
                }
                if (Thread.currentThread().isInterrupted) {
                    break
                }
                byteData?.clear()
                playbackListener?.onNeedFillBuffer(byteData)
                val data = byteData?.array()
                Log.d("PlayerProcess", "run() bytes ${byteData} data ${data}")
                if (data != null) {
                    try {
                        audioPlayer?.write(data, 0, data.size)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
            try {
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (audioPlayer != null) {
                if (audioPlayer!!.state != AudioTrack.PLAYSTATE_STOPPED) {
                    audioPlayer?.stop()
                    audioPlayer?.release()
                    mThread = null
                }
            }
        }
    }

    fun pause() {
        audioPlayer?.pause()
        pause = true
    }

    fun stop() {
        isPlay = false
        pause = false
        audioPlayer?.stop()
        audioPlayer?.release()
        audioPlayer = null
        audioEffect?.release()
        audioEffect = null
    }


    interface PlaybackListener {
        fun onNeedFillBuffer(byteData: ByteBuffer?)
    }
}
