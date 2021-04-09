
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.firebase

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

/**
 * Used to store firebase specific info.
 */
internal class TokenStorage(
    private val preferences: SharedPreferences,
    private val appId: String,
) {
    internal var token: String?
        get() = preferences.getString("$TOKEN-$appId", null)
        set(value) {
            Log.d("TOKEN", "Token value: $value")
            preferences.edit {
                putString("$TOKEN-$appId", value)
            }
        }

    companion object {
        private const val TOKEN = "token"
    }
}
