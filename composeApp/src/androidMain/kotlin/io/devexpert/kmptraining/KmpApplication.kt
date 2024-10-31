package io.devexpert.kmptraining

import android.app.Application

class KmpApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppInitializer.onApplicationStart()
    }
}
