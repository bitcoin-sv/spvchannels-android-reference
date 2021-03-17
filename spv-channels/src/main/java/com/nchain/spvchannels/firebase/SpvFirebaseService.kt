package com.nchain.spvchannels.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.NotificationParams
import com.nchain.spvchannels.R
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
open class SpvFirebaseService : FirebaseMessagingService() {
    private val notificationId = Random.nextInt()

    override fun onCreate() {
        super.onCreate()
        createSpvChannel()
    }

    override fun handleIntent(intent: Intent?) {
        try {
            FirebaseApp.getInstance()
            // The host app already has their firebase set up, delegate to that instead
            super.handleIntent(intent)
        } catch (e: IllegalStateException) {
            // The host app does not have their own firebase set up, handle the intent here
            showNotification(intent ?: return)
        }
    }

    private fun createSpvChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val name = getString(R.string.title_channel_name)
        val channel =
            NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }

    private fun showNotification(intent: Intent) {
        val extras = intent.extras ?: return
        if (!NotificationParams.isNotification(extras)) return

        val notificationParams = NotificationParams(extras)
        val title = notificationParams.getString("gcm.notification.title")
        val body = notificationParams.getString("gcm.notification.body")

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(applicationInfo.icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        getSystemService<NotificationManager>()?.notify(notificationId, notification)
    }

    companion object {
        private const val CHANNEL_ID = "spv_channels"
    }
}
