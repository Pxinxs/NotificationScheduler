package com.test.scheduler.config

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.test.scheduler.db.AppDatabase
import com.test.scheduler.managers.DatabaseManager
import com.test.scheduler.presenter.AddNotificationPresenter
import com.test.scheduler.presenter.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun provideDatabaseManager(database: AppDatabase): DatabaseManager =
            DatabaseManager(database)

    @Provides
    @Singleton
    fun provideAddNotificationPresenter(databaseManager: DatabaseManager): AddNotificationPresenter =
            AddNotificationPresenter(databaseManager)

    @Provides
    @Singleton
    fun provideMainPresenter(databaseManager: DatabaseManager): MainPresenter =
            MainPresenter(databaseManager)

}