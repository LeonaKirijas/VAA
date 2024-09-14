package com.example.vaa.util

import android.content.Context
import android.content.SharedPreferences

class TutorialManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("tutorial_prefs", Context.MODE_PRIVATE)

    fun isFirstTimeUser(): Boolean {
        return sharedPreferences.getBoolean("is_first_time", true)
    }

    fun markTutorialComplete() {
        sharedPreferences.edit().putBoolean("is_first_time", false).apply()
    }
}
