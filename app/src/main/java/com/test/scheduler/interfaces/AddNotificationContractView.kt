package com.test.scheduler.interfaces

import java.util.*

interface AddNotificationContractView {
    fun setCalendar(calendar: Calendar)

    fun onAddedSuccess(note: String)
    fun onAddedError()
}