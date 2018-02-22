package com.test.scheduler.presenter

import com.test.scheduler.entity.room.Notification
import com.test.scheduler.interfaces.AddNotificationContractView
import com.test.scheduler.managers.DatabaseManager
import java.util.*

class AddNotificationPresenter(private val databaseManager: DatabaseManager) {

    private var view: AddNotificationContractView? = null

    private var calendar: Calendar? = null

    fun attachView(contractView: AddNotificationContractView, isRestoreCalendar: Boolean) {
        view = contractView
        if (calendar == null || !isRestoreCalendar) {
            calendar = Calendar.getInstance()
        }

        calendar?.let { view?.setCalendar(it) }
    }

    fun updateCalendar(calendar: Calendar) {
        this.calendar = calendar
    }

    fun addNewNotification(notification: Notification) {
        databaseManager.saveNotification(notification)
                .subscribe({
                    view?.onAddedSuccess(notification.note)
                }, {
                    view?.onAddedError()
                })
    }

    fun detachView() {
        view = null
    }
}