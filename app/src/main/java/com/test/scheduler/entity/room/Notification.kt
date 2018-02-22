package com.test.scheduler.entity.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "notification")
data class Notification(
        @ColumnInfo(name = "date")
        var date: String,
        @ColumnInfo(name = "time")
        var time: String,
        @ColumnInfo(name = "note")
        var note: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0)