// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import io.bitcoinsv.spvchannels.notifications.models.TokenRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Creates a firebase configuration to use for background notifications.
 */
class FirebaseConfig private constructor(
    private val messaging: FirebaseMessaging,
    private val storage: TokenStorage,
) {
    var updater: (suspend (String, TokenRequest) -> Response<Void>)? = null
    var updateJob: Job? = null

    init {
        messaging.isAutoInitEnabled = true
        updateTokenIfNeeded()
    }

    /**
     * Updates the token if it does not match the current one.
     */
    internal fun updateTokenIfNeeded() {
        if (updateJob?.isActive == true) return

        val oldToken = storage.token ?: return
        messaging.token.addOnSuccessListener {
            if (oldToken == it) return@addOnSuccessListener
            val updater = updater ?: return@addOnSuccessListener

            storage.token = it
            updateJob = GlobalScope.launch(Dispatchers.IO) {
                updater(oldToken, TokenRequest(it))
            }
        }
    }

    /**
     * Returns the current FCM token, fetching it if needed
     */
    internal suspend fun fetchToken(): String = suspendCoroutine { continuation ->
        messaging.token.addOnSuccessListener {
            continuation.resume(it)
        }
    }

    /**
     * Creates a new builder for FirebaseConfig. Requires either [FirebaseMessaging] or [projectId],
     * [applicationId] and [apiKey].
     */
    class Builder(private val context: Context) {
        private var projectId: String? = null
        private var applicationId: String? = null
        private var apiKey: String? = null
        private var messaging: FirebaseMessaging? = null

        /**
         * Sets the firebase project ID. Only required if [FirebaseMessaging] is not set.
         *
         * @param id
         */
        fun projectId(id: String): Builder {
            this.projectId = id
            return this
        }

        /**
         * Sets the application ID. Only required if [FirebaseMessaging] is not set.
         *
         * @param applicationId
         */
        fun applicationId(applicationId: String): Builder {
            this.applicationId = applicationId
            return this
        }

        /**
         * Sets the API key. Only required if [FirebaseMessaging] is not set.
         */
        fun apiKey(apiKey: String): Builder {
            this.apiKey = apiKey
            return this
        }

        /**
         * Sets the FirebaseMessaging instance to use. Only required if [projectId],
         * [applicationId] and [apiKey] are not set.
         */
        fun messaging(messaging: FirebaseMessaging): Builder {
            this.messaging = messaging
            return this
        }

        /**
         * Creates a new instance of [FirebaseConfig].
         */
        fun build(): FirebaseConfig {
            val messaging = if (messaging != null) {
                messaging!!
            } else {
                val projectId = projectId
                val applicationId = applicationId
                val apiKey = apiKey
                if (projectId == null || applicationId == null || apiKey == null) {
                    throw IllegalStateException(
                        "Project ID, application ID and api key need to be provided."
                    )
                }
                val options = FirebaseOptions.Builder()
                    .setProjectId(projectId)
                    .setApplicationId(applicationId)
                    .setApiKey(apiKey)
                    .build()
                val app = FirebaseApp.initializeApp(context, options, CONFIG_NAME)
                app.get(FirebaseMessaging::class.java)
            }
            return FirebaseConfig(
                messaging,
                TokenStorage(
                    context.getSharedPreferences(applicationId, Context.MODE_PRIVATE),
                    applicationId ?: "default"
                )
            )
        }
    }

    companion object {
        internal const val CONFIG_NAME = "spv_channels"
    }
}
