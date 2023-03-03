package com.metalichesky.zvuk.generator.di

import dagger.Module


@Module(
    includes = [
        StorageModule::class,
        AudioModule::class,
        ViewModelModule::class,
        CoroutinesDispatchersModule::class,
        CoroutinesScopesModule::class
    ]
)
abstract class AppModule