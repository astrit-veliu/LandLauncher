package io.github.astrit_veliu.landlauncher.common.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserPreferences {
    override fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    override fun clear(vararg keys: String) {
        keys.forEach {
            sharedPreferences.edit().remove(it).apply()
        }
    }

    override fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
}