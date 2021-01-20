package io.github.astrit_veliu.landlauncher.utils

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_LAUNCHER
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_PERMISSIONS
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import io.github.astrit_veliu.landlauncher.data.ApplicationEntity


@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
@SuppressLint("QueryPermissionsNeeded")
fun getInstalledApps(context: Context): List<ApplicationEntity> {
    val pm: PackageManager = context.packageManager
    val apps: MutableList<ApplicationEntity> = ArrayList()
    val packs: List<PackageInfo> = pm.getInstalledPackages(0)
    //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
    for (i in packs.indices) {
        val p = packs[i]
        if (!isSystemPackage(p)) {
            val appName = p.applicationInfo.loadLabel(pm).toString()
            val icon = p.applicationInfo.loadIcon(pm)
            val banner = p.applicationInfo.loadBanner(pm)
            val packageName = p.applicationInfo.packageName
            apps.add(ApplicationEntity(
                    appName = appName,
                    icon = icon,
                    banner = banner,
                    packageName = packageName
            )
            )
        }
    }
    return apps
}


@SuppressLint("QueryPermissionsNeeded", "WrongConstant")
fun extractAppLaunchInfo(context: Context): List<ApplicationEntity> {
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null).also {
        it.addCategory(CATEGORY_LAUNCHER)
    }

    return packageManager.queryIntentActivities(intent, GET_PERMISSIONS).map {
        ApplicationEntity(
            appName = it.loadLabel(packageManager).toString(),
            icon = it.activityInfo.loadIcon(packageManager),
            banner = it.activityInfo.loadBanner(packageManager),
            packageName = it.activityInfo.packageName
        )
    }.sortedBy { it.appName.toLowerCase() }
}

fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
    return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
}

fun setLightNavigationBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val view = activity.window.decorView.findViewById<View>(R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        view.systemUiVisibility = flags
        activity.window.navigationBarColor = ContextCompat.getColor(activity.applicationContext, R.color.white)
    }
}


fun setDarkNavigationBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val view = activity.window.decorView.findViewById<View>(R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        view.systemUiVisibility = flags
        // activity.window.navigationBarColor = ContextCompat.getColor(activity.applicationContext, R.color.colorPrimaryDark)
    }
}


fun setDarkNavigationBarHome(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val view = activity.window.decorView.findViewById<View>(R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        view.systemUiVisibility = flags
        // activity.window.navigationBarColor = ContextCompat.getColor(activity.applicationContext, R.color.colorDark)
    }
}