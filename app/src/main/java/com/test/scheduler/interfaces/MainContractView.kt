package com.test.scheduler.interfaces

import com.test.scheduler.entity.room.Notification

interface MainContractView {
    fun showNotifications(notifications: List<Notification>)
    fun readNotificationsError()
}