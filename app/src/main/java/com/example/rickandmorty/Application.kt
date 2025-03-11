package com.example.rickandmorty
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