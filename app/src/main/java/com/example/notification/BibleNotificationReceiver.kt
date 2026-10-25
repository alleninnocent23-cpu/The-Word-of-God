package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BibleNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            BibleNotificationManager.scheduleTwoHourNotifications(context)
        } else {
            BibleNotificationManager.sendScriptureNotification(context)
        }
    }
}
