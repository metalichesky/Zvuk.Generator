package com.metalichesky.zvuk.generator.di

import android.content.Context
import com.metalichesky.zvuk.generator.App
import com.metalichesky.zvuk.storage.AlphabetDataSource
import com.metalichesky.zvuk.storage.UserPrefsDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope

@Module
class StorageModule {

    @Provides
    fun provideUserPrefsDataStore(app: App): UserPrefsDataSource {
        return UserPrefsDataSource(app)
    }

    @Provides
    fun provideAlphabetDataSource(): AlphabetDataSource {
        return AlphabetDataSource()
    }

}