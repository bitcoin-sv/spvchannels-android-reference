package com.nchain.spvchannels.host

import android.content.Context
import com.nchain.spvchannels.SpvChannelsSdk
import com.nchain.spvchannels.firebase.FirebaseConfig
import dagger.hilt.android.qualifiers.ApplicationContext
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
