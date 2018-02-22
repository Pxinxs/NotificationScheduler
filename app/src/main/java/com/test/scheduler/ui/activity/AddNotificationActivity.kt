package com.test.scheduler.ui.activity

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.test.scheduler.App
import com.test.scheduler.R
import com.test.scheduler.entity.room.Notification
import com.test.scheduler.interfaces.AddNotificationContractView
import com.test.scheduler.presenter.AddNotificationPresenter
import com.test.scheduler.receiver.NotificationReceiver
import com.test.scheduler.utils.DateUtils
import com.test.scheduler.utils.General.Companion.ACTIVITY_RESULT_CODE
import com.test.scheduler.utils.General.Companion.ALARM_MANAGER_RESULT_CODE
import com.test.scheduler.utils.toast
import kotlinx.android.synthetic.main.activity_add_notification.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject

class AddNotificationActivity : AppCompatActivity(), View.OnClickListener, AddNotificationContractView {

    @Inject
    lateinit var presenter: AddNotificationPresenter

    private var alarmManager: AlarmManager? = null
    private lateinit var calendar: Calendar

    companion object {
        private const val RESTORE_CALENDAR_STATE = "restore_calendar_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notification)
        App.component.inject(this)

        initToolbar()

        var isRestoreCalendar = false
        savedInstanceState?.let {
            isRestoreCalendar = it.getBoolean(RESTORE_CALENDAR_STATE, false)
        }

        presenter.attachView(this, isRestoreCalendar)

        clDate.setOnClickListener(this)
        clTime.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.add_notification)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { super.onBackPressed() }
    }

    override fun onClick(view: View?) {
        when (view) {
            clDate -> {
                showDatePicker()
            }
            clTime -> {
                showTimePicker()
            }
            btnAdd -> {
                val date = tvDate.text.toString()
                val time = tvTime.text.toString()
                val note = etTitle.text.toString()

                presenter.addNewNotification(Notification(date, time, note))
            }
        }
    }

    override fun setCalendar(calendar: Calendar) {
        this.calendar = calendar

        tvDate.text = DateUtils().getDate(calendar)
        tvTime.text = DateUtils().getTime(calendar)
    }

    private fun showDatePicker() {
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    presenter.updateCalendar(calendar)

                    tvDate.text = DateUtils().getDate(calendar)
                }, mYear, mMonth, mDay)
        datePicker.window.attributes.windowAnimations = R.style.BottomDialogAnimation
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()

    }

    private fun showTimePicker() {
        val mHour = calendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    presenter.updateCalendar(calendar)

                    tvTime.text = DateUtils().getTime(calendar)
                }, mHour, mMinute, true)
        timePicker.window.attributes.windowAnimations = R.style.BottomDialogAnimation
        timePicker.show()
    }

    private fun setNotification(note: String) {
        val notificationIntent = Intent(this, NotificationReceiver::class.java)
                .putExtra("note", note)
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_MANAGER_RESULT_CODE,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun onAddedSuccess(note: String) {
        setNotification(note)

        val intent = Intent()
        setResult(ACTIVITY_RESULT_CODE, intent)
        finish()
    }

    override fun onAddedError() {
        toast(getString(R.string.error_save_notification))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        // restore date and time (for change orientation as sample)
        outState?.putBoolean(RESTORE_CALENDAR_STATE, true)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
