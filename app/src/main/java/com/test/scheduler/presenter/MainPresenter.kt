package com.test.scheduler.presenter

import com.test.scheduler.interfaces.MainContractView
import com.test.scheduler.managers.DatabaseManager

class MainPresenter(private val databaseManager: DatabaseManager) {

    private var view: MainContractView? = null

    fun attachView(contractView: MainContractView) {
        view = contractView

        getNotifications()
    }

    private fun getNotifications() {
        databaseManager.getNotifications()
                .subscribe({
                    view?.showNotifications(it)
                }, {
                    view?.readNotificationsError()
                })
    }

    fun clearNotifications() {
        databaseManager.clearNotifications()
    }

    fun detach() {
        view = null
    }
}