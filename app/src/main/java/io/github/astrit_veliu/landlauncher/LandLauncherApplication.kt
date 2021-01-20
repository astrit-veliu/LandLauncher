package io.github.astrit_veliu.landlauncher

import android.app.Application
import android.content.Context
import io.github.astrit_veliu.landlauncher.utils.UserPreferences

class LandLauncherApplication : Application() {

    private var context: Context? = null
    private var userPreferences: UserPreferences? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        userPreferences = UserPreferences(applicationContext)
    }

    fun launcherContext() : Context? = context

    fun userPreferences() : UserPreferences? = userPreferences

}
