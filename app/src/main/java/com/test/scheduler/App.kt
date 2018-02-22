package com.test.scheduler

import android.app.Application
import com.test.scheduler.config.AppComponent
import com.test.scheduler.config.AppModule
import com.test.scheduler.config.DaggerAppComponent

class App: Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}