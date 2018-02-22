package com.test.scheduler.managers

import com.test.scheduler.db.AppDatabase
import com.test.scheduler.entity.room.Notification
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DatabaseManager(private val database: AppDatabase) {

    fun getNotifications(): Flowable<List<Notification>> {
        return database.notificationDao().getNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveNotification(notification: Notification): Completable {
        return Completable.fromAction {
            database.notificationDao().insert(notification)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun clearNotifications(): Boolean {
        return CompositeDisposable().add(
                Observable.fromCallable {
                    database.notificationDao().deleteTable()
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe())
    }
}