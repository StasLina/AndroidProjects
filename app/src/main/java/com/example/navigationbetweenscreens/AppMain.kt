package com.example.navigationbetweenscreens

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppMain : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}