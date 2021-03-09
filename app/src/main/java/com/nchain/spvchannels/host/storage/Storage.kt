package com.nchain.spvchannels.host.storage

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Storage @Inject constructor(@ApplicationContext context: Context) {
    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    var url: String
        get() = preferences.getString(URL, null) ?: ""
        set(value) = preferences.edit { putString(URL, value) }

    var account: String
        get() = preferences.getString(ACCOUNT, null) ?: ""
        set(value) = preferences.edit { putString(ACCOUNT, value) }

    var username: String
        get() = preferences.getString(USERNAME, null) ?: ""
        set(value) = preferences.edit { putString(USERNAME, value) }

    var password: String
        get() = preferences.getString(PASSWORD, null) ?: ""
        set(value) = preferences.edit { putString(PASSWORD, value) }

    companion object {
        private const val NAME = "input_storage"
        private const val URL = "url"
        private const val ACCOUNT = "account"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }
}