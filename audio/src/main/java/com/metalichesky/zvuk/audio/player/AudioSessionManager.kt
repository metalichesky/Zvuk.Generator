package com.metalichesky.zvuk.audio.player

import android.content.Context
import android.media.AudioManager

class AudioSessionManager(
    private val context: Context
) {

    fun generateAudioSessionId(): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        return audioManager?.generateAudioSessionId() ?: AudioManager.ERROR
    }

}