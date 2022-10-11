package com.metalichesky.zvuk.generator

import android.app.Application
import com.metalichesky.zvuk.generator.di.AppComponent
import com.metalichesky.zvuk.generator.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class App: Application() {
    companion object {
        lateinit var instance: App
    }

    var appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private set

    lateinit var appComponent: AppComponent
    private set

    override fun onCreate() {
        instance = this
        super.onCreate()

        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .app(this)
            .build()
        appComponent.inject(this)

    }

}