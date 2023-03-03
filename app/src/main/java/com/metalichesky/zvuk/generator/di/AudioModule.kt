package com.metalichesky.zvuk.generator.di

import com.metalichesky.zvuk.audio.player.AudioSessionManager
import com.metalichesky.zvuk.generator.App
import dagger.Module
import dagger.Provides

@Module
class AudioModule {

    @Provides
    fun provideAudioSessionManager(app: App): AudioSessionManager {
        return AudioSessionManager(app)
    }

}