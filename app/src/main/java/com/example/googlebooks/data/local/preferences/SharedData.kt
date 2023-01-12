package com.example.googlebooks.data.local.preferences

import android.content.Context
import com.securepreferences.SecurePreferences
import javax.inject.Inject

class SharedData @Inject constructor(
    private val context: Context
) {
    private val prefs = SecurePreferences(context, "MyPrefs", "ibrohim")

    private val FIRST_NAME = "first_name"
    private val LAST_NAME = "last_name"
    private val AGE = "age"

    var firtName: String
        get() = prefs.getString(FIRST_NAME, "")!!
        set(token) = prefs.edit().putString(FIRST_NAME, token).apply()

    var lastName: String
        get() = prefs.getString(LAST_NAME, "")!!
        set(token) = prefs.edit().putString(LAST_NAME, token).apply()

    var age: String
        get() = prefs.getString(AGE, "")!!
        set(token) = prefs.edit().putString(AGE, token).apply()

}