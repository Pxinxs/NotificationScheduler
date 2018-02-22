package com.test.scheduler.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.test.scheduler.App
import com.test.scheduler.R
import com.test.scheduler.entity.room.Notification
import com.test.scheduler.interfaces.MainContractView
import com.test.scheduler.presenter.MainPresenter
import com.test.scheduler.ui.adapters.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.support.design.widget.Snackbar
import com.test.scheduler.receiver.NotificationReceiver
import com.test.scheduler.utils.General.Companion.ACTIVITY_RESULT_CODE
import com.test.scheduler.utils.General.Companion.ALARM_MANAGER_RESULT_CODE
import com.test.scheduler.utils.toast

class MainActivity : AppCompatActivity(), View.OnClickListener, MainContractView {

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)

        presenter.attachView(this)

        fabAdd.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            fabAdd -> {
                startActivityForResult(Intent(this, AddNotificationActivity::class.java), ACTIVITY_RESULT_CODE)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    override fun showNotifications(notifications: List<Notification>) {
        val adapter = MainAdapter(notifications)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter
        rvMain.visibility = View.VISIBLE
    }

    override fun readNotificationsError() {
       toast(getString(R.string.error_read_notifications_form_db))
    }

    private fun cancelNotifications() {
        val intent = Intent(this, NotificationReceiver::class.java)
        val sender = PendingIntent.getBroadcast(this, ALARM_MANAGER_RESULT_CODE, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(sender)
    }

    private fun showSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        val snackBar = Snackbar.make(coordinatorLayout, message, length)
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.nav_clear -> {
                cancelNotifications()           // disable alarm notifications
                presenter.clearNotifications()  // clear from database
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return
        if (resultCode == ACTIVITY_RESULT_CODE) {
            showSnackBar(getString(R.string.notification_was_added))
        }
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }
}
