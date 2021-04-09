// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.firebase

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService

/**
 * The default service implementation. If Firebase is being used in the main project this service
 * does not need to be declared in AndroidManifest.xml.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class DefaultSpvMessagingService : FirebaseMessagingService()
