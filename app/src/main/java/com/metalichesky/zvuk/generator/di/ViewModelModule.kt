package com.metalichesky.zvuk.generator.di

import androidx.lifecycle.ViewModel
import com.metalichesky.zvuk.generator.viewmodel.SoundGeneratorViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
interface ViewModelModule {

    @Binds
    @[IntoMap ViewModelKey(SoundGeneratorViewModel::class)]
    fun bindsSoundGeneratorViewModel(viewModel: SoundGeneratorViewModel): ViewModel


}