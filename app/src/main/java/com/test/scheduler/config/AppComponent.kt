package com.test.scheduler.config

import com.test.scheduler.ui.activity.AddNotificationActivity
import com.test.scheduler.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(addNotificationActivity: AddNotificationActivity)
}