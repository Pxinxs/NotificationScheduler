package com.test.scheduler.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.test.scheduler.entity.room.Notification

@Database(entities = [(Notification::class)], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}