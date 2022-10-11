package com.metalichesky.zvuk.generator.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
object CoroutinesScopesModule {

    @Singleton
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + defaultDispatcher)
    }

}