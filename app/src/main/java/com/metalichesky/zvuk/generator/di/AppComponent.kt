package com.metalichesky.zvuk.generator.di

import com.metalichesky.zvuk.generator.App
import com.metalichesky.zvuk.generator.view.SoundGeneratorFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Suppress("unused")
@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: App): Builder

        fun build(): AppComponent

    }

    fun inject(app: App)

    fun inject(fragment: SoundGeneratorFragment)

}