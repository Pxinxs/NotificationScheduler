package com.test.scheduler.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.test.scheduler.entity.room.Notification
import io.reactivex.Flowable

@Dao
interface NotificationDao {
    @Query("SELECT * from notification")
    fun getNotifications(): Flowable<List<Notification>>

    @Insert
    fun insert(notification: Notification)

    @Query("DELETE FROM notification")
    fun deleteTable()
}