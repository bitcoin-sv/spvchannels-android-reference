// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.bitcoinsv.spvchannels.R
import io.bitcoinsv.spvchannels.notifications.Notification
import java.lang.ref.WeakReference

/**
 * The default service implementation. If Firebase is being used in the main project this service
 * does not need to be declared in AndroidManifest.xml.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
open class DefaultSpvMessagingService : FirebaseMessagingService() {
    lateinit var notificationManager: NotificationManagerCompat
    private var isInBackground = false

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun enteredForeground() {
                isInBackground = false
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun enteredBackground() {
                isInBackground = true
            }
        })
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (isSpvNotification(message)) {
            handleSpvNotification(message)
        }
    }

    /**
     * Checks whether or not the remote message is SPV notification and should be handled
     * internally.
     *
     * @param message the message to check
     */
    protected fun isSpvNotification(message: RemoteMessage): Boolean {
        return message.data.containsKey(CHANNEL) &&
            message.notification?.title != null &&
            message.notification?.body != null
    }

    /**
     * Handles the SPV notification, showing it if required.
     *
     * @throws IllegalArgumentException if the remote message does not contain time, message and
     * channelId
     */
    protected open fun handleSpvNotification(remoteMessage: RemoteMessage) {
        val time = remoteMessage.notification?.body
        val message = remoteMessage.notification?.title
        val channel = remoteMessage.data[CHANNEL]

        if (time == null || message == null || channel == null) {
            throw IllegalArgumentException("Invalid message: time, message and channelId required")
        }

        if (isInBackground) {
            showNotification(message)
        } else {
            notifyListeners(time, message, channel)
        }
    }

    private fun notifyListeners(time: String, message: String, channel: String) {
        NOTIFICATION_CALLBACKS[channel]?.get()?.let {
            it(Notification(time, message))
        }
    }

    /**
     * Shows the notification in channel returned by [getNotificationChannel] with notificationId
     * returned by [getNotificationId].
     */
    private fun showNotification(message: String) {
        val notification = NotificationCompat.Builder(this, getNotificationChannel())
            .setSmallIcon(getSmallIcon())
            .setContentTitle(getString(R.string.title_notification))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notificationManager.notify(getNotificationId(), notification)
    }

    /**
     * Returns the notification ID. Override to set a custom one.
     */
    protected open fun getNotificationId() = 2042020

    /**
     * Returns the notification icon (defaults to app icon). Override to set a custom one.
     */
    protected open fun getSmallIcon() = applicationContext.applicationInfo.icon

    /**
     * Returns the OS channel for SPV notifications. By default, all notifications will be grouped
     * in common notification channel called "SPV".
     */
    protected open fun getNotificationChannel(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ANDROID_CHANNEL_ID,
                getString(R.string.title_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        return ANDROID_CHANNEL_ID
    }

    companion object {
        private val NOTIFICATION_CALLBACKS =
            HashMap<String, WeakReference<(Notification) -> Unit>>()
        const val ANDROID_CHANNEL_ID = "spv_channels"
        const val CHANNEL = "channelId"

        /**
         * Register channel for notifications, keeping a weak reference to avoid memory leaks.
         */
        internal fun registerForNotifications(channel: String, callback: (Notification) -> Unit) {
            NOTIFICATION_CALLBACKS[channel] = WeakReference(callback)
        }
    }
}
