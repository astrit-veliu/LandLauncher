package io.github.astrit_veliu.landlauncher.common.utils

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(private val context: Context) {

    private val sharedPreferences: SharedPreferences

    fun setNightMode(state: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("NightMode", state!!)
        editor.apply()
    }

    fun loadNightMode(): Boolean {
        return sharedPreferences.getBoolean("NightMode", false)
    }

    init {
        sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE)
    }
}