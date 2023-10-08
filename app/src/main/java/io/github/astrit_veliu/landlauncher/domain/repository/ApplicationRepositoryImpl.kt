package io.github.astrit_veliu.landlauncher.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_PERMISSIONS
import android.os.Build
import io.github.astrit_veliu.landlauncher.common.preferences.UserPreferences
import io.github.astrit_veliu.landlauncher.domain.model.Application
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory.GAME
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory.MEDIA
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory.OTHERS
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory.SOCIAL
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory.UTILS
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val userPreferences: UserPreferences
) : ApplicationRepository {

    private var applicationMap: Map<ApplicationCategory, List<Application>> = emptyMap()

    @SuppressLint("WrongConstant")
    override suspend fun getAllPackages(): Map<ApplicationCategory, List<Application>> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).also { it.addCategory(Intent.CATEGORY_LAUNCHER) }
        applicationMap = packageManager.queryIntentActivities(intent, GET_PERMISSIONS).map {
            Application(
                appName = it.loadLabel(packageManager).toString(),
                icon =  it.loadIcon(packageManager),
                iconResource = it.activityInfo.icon,
                banner = it.activityInfo.loadBanner(packageManager),
                packageName = it.activityInfo.packageName,
                category = getApplicationCategory(it.activityInfo.packageName)
            )
        }.groupBy { it.category ?: OTHERS }
        return applicationMap
        //.sortedBy { it.appName.lowercase() }
    }

    override suspend fun getPackagesByCategory(category: ApplicationCategory): List<Application> {
        return if (applicationMap.isNullOrEmpty()) getAllPackages()[category] ?: emptyList()
        else applicationMap[category] ?: emptyList()
    }

    private fun getApplicationCategory(packageName: String): ApplicationCategory {
        return try {
            val info: ApplicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                when (info.category) {
                    ApplicationInfo.CATEGORY_UNDEFINED -> OTHERS
                    ApplicationInfo.CATEGORY_GAME -> GAME
                    ApplicationInfo.CATEGORY_AUDIO -> MEDIA
                    ApplicationInfo.CATEGORY_VIDEO -> MEDIA
                    ApplicationInfo.CATEGORY_IMAGE -> MEDIA
                    ApplicationInfo.CATEGORY_SOCIAL -> SOCIAL
                    ApplicationInfo.CATEGORY_NEWS -> SOCIAL
                    ApplicationInfo.CATEGORY_MAPS -> UTILS
                    ApplicationInfo.CATEGORY_PRODUCTIVITY -> UTILS
                    else -> OTHERS
                }
            } else {
                @Suppress("DEPRECATION")
                if (info.flags and ApplicationInfo.FLAG_IS_GAME == ApplicationInfo.FLAG_IS_GAME) GAME
                else OTHERS
            }
        } catch (e: PackageManager.NameNotFoundException) {
            OTHERS
        }
    }
}