package com.test.scheduler.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.test.scheduler.R
import com.test.scheduler.ui.activity.MainActivity
import com.test.scheduler.utils.General.Companion.ALARM_MANAGER_RESULT_CODE

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        var note = ""
        intent?.extras?.let {
            note = it.getString("note")
        }

        setNotification(context, note)
    }

    private fun setNotification(context: Context, note: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(context, ALARM_MANAGER_RESULT_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val chanelId = "simple_notification"
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, chanelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.notification))
                .setContentText(note)
                .setAutoCancel(true)
                .setSound(defaultSound)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(chanelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(111, notificationBuilder.build())
    }
}