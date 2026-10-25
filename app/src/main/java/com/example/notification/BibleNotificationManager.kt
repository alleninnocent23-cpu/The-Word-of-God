package com.example.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

object BibleNotificationManager {

    const val CHANNEL_ID = "word_of_god_scripture_channel"
    private const val NOTIFICATION_ID = 777
    const val ACTION_TRIGGER_NOTIFICATION = "com.example.ACTION_BIBLE_NOTIFICATION"

    val inspirationalVerses = listOf(
        "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life." to "John 3:16",
        "The LORD is my shepherd; I shall not want." to "Psalm 23:1",
        "I can do all things through Christ which strengtheneth me." to "Philippians 4:13",
        "Trust in the LORD with all thine heart; and lean not unto thine own understanding." to "Proverbs 3:5",
        "Thy word is a lamp unto my feet, and a light unto my path." to "Psalm 119:105",
        "They that wait upon the LORD shall renew their strength; they shall mount up with wings as eagles." to "Isaiah 40:31",
        "Where you go I will go, and where you stay I will stay. Your people will be my people and your God my God." to "Ruth 1:16",
        "Peace I leave with you, my peace I give unto you: not as the world giveth, give I unto you." to "John 14:27"
    )

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/angelic_sound")
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                "WORD OF GOD Scripture Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Angelic voice and chime Scripture reminders every 2 hours"
                enableVibration(true)
                setSound(soundUri, audioAttributes)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleTwoHourNotifications(context: Context) {
        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BibleNotificationReceiver::class.java).apply {
            action = ACTION_TRIGGER_NOTIFICATION
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intervalMillis = 2 * 60 * 60 * 1000L // 2 Hours
        val triggerAtMillis = System.currentTimeMillis() + intervalMillis

        try {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                intervalMillis,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendScriptureNotification(context: Context, verseOverride: String? = null, refOverride: String? = null) {
        createNotificationChannel(context)

        val pair = if (verseOverride != null && refOverride != null) {
            verseOverride to refOverride
        } else {
            inspirationalVerses.random()
        }

        val verseText = pair.first
        val reference = pair.second

        // Play angelic chime audio
        try {
            val mediaPlayer = MediaPlayer.create(context, R.raw.angelic_sound)
            mediaPlayer?.setOnCompletionListener { it.release() }
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/angelic_sound")

        val largeIcon = try {
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_bible_cover)
        } catch (e: Exception) {
            null
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bible_notification) // Bible icon
            .setLargeIcon(largeIcon)
            .setContentTitle("WORD OF GOD — $reference")
            .setContentText("\"$verseText\"")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("\"$verseText\"\n\n— $reference\n\nMay the peace of the Lord abide with you.")
                    .setSummaryText("WORD OF GOD Daily Reminder")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 250, 200, 250))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
