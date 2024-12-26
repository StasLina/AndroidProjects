package com.example.gitchecker
import android.app.Application
import android.util.Log
import com.example.gitchecker.models.AppData
import com.example.gitchecker.models.AppDataMain
import com.example.gitchecker.models.HardData

class MyApplication : Application() {
    private lateinit var appData: AppDataMain

    override fun onCreate() {
        super.onCreate()
        appData = AppDataMain()
        Log.d("dag","Value detect21323 = ${appData}")
    }

    fun GetData() : AppDataMain {return appData}

    override fun onTerminate() {
        super.onTerminate()
    }
}