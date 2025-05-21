package com.example.maproutebuilder

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import com.yandex.maps.mobile.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class AppMain : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initializeMapKitAsync()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    private fun initializeMapKitAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            Timber.v("Yandex API_KEY: ${com.example.maproutebuilder.BuildConfig.YANDEX_MAPKIT_API_KEY}")

            MapKitFactory.setApiKey(com.example.maproutebuilder.BuildConfig.YANDEX_MAPKIT_API_KEY)
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
        }
    }
}