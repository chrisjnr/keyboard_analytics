package com.example.threatfabricassessment

import android.app.Application
import com.example.threatfabricassessment.modules.appModule
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}