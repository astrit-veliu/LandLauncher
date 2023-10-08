package io.github.astrit_veliu.landlauncher.common.preferences

interface UserPreferences {

    fun putString(key: String, value: String?)

    fun getString(key: String): String

    fun putBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun putInt(key: String, value: Int)

    fun getInt(key: String, defaultValue: Int): Int

    fun putLong(key: String, value: Long)

    fun getLong(key: String, defaultValue: Long): Long

    fun clear(vararg keys: String)

    fun clearAll()

    fun contains(key: String): Boolean
}