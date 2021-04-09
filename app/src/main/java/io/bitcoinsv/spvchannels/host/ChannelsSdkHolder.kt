
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bitcoinsv.spvchannels.SpvChannelsSdk
import io.bitcoinsv.spvchannels.firebase.FirebaseConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelsSdkHolder @Inject constructor(@ApplicationContext private val context: Context) {
    private val sdkHolder = mutableMapOf<String, SpvChannelsSdk>()

    private val firebaseConfig = FirebaseConfig.Builder(context)
        .apiKey(BuildConfig.FIREBASE_API_KEY)
        .applicationId(BuildConfig.FIREBASE_APP_ID)
        .projectId(BuildConfig.FIREBASE_PROJECT_ID)
        .build()

    fun sdkForUrl(baseUrl: String): SpvChannelsSdk {
        return sdkHolder.getOrPut(baseUrl) { SpvChannelsSdk(context, firebaseConfig, baseUrl) }
    }
}
