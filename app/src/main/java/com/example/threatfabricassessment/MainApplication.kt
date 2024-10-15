package com.example.threatfabricassessment

import android.app.Application
import com.example.db.module.dbModule
import com.example.threatfabricassessment.modules.appModule
import com.example.threatfabricassessment.modules.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(appModule, dbModule, domainModule)
        }
    }
}
