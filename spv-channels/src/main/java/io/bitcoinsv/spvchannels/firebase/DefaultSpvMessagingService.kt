package io.bitcoinsv.spvchannels.firebase

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService

/**
 * The default service implementation. If Firebase is being used in the main project this service
 * does not need to be declared in AndroidManifest.xml.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class DefaultSpvMessagingService : FirebaseMessagingService()
