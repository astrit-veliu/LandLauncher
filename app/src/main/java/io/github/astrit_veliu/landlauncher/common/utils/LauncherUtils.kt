package io.github.astrit_veliu.landlauncher.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_LAUNCHER
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_PERMISSIONS
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import io.github.astrit_veliu.landlauncher.data.ApplicationEntity


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
            val iconResource =p.applicationInfo.icon
            val banner = p.applicationInfo.loadBanner(pm)
            val packageName = p.applicationInfo.packageName
            apps.add(
                ApplicationEntity(
                    appName = appName,
                    icon = icon,
                    iconResource = iconResource,
                    banner = banner,
                    packageName = packageName
                )
            )
        }
    }
    return apps
}

fun packageIsGame(context: Context, packageName: String): Boolean {
    return try {
        val info: ApplicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            info.category == ApplicationInfo.CATEGORY_GAME
        } else {
            // We are suppressing deprecation since there are no other options in this API Level
            @Suppress("DEPRECATION")
            (info.flags and ApplicationInfo.FLAG_IS_GAME) == ApplicationInfo.FLAG_IS_GAME
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("Util", "Package info not found for name: " + packageName, e)
        // Or throw an exception if you want
        false
    }
}

fun Context.handlePackage(packageName: String?) {
    packageName?.let {
        val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, it)
        if (intent.resolveActivity(packageManager) == null) Toast.makeText(this, "Application not found", Toast.LENGTH_LONG).show()
        else startActivity(intent)
    }
}

fun Context.openPackage(packageName: String?) {
    packageName?.let { startActivity(packageManager.getLaunchIntentForPackage(it)) }
}

fun openPlayStore(context: Context) {
    val pm = context.packageManager
    val launchIntent = pm.getLaunchIntentForPackage("com.android.vending")
    if (launchIntent != null) context.startActivity(launchIntent)
    else Toast.makeText(context, "PlayStore not installed", Toast.LENGTH_SHORT).show()
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
            iconResource = it.activityInfo.iconResource,
            banner = it.activityInfo.loadBanner(packageManager),
            packageName = it.activityInfo.packageName
        )
    }.sortedBy { it.appName.lowercase() }
}

@SuppressLint("QueryPermissionsNeeded", "WrongConstant")
fun getGames(context: Context): List<ApplicationEntity> {
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null).also {
        it.addCategory(CATEGORY_LAUNCHER)
    }

    return packageManager.queryIntentActivities(intent, GET_PERMISSIONS).map {
        ApplicationEntity(
            appName = it.loadLabel(packageManager).toString(),
            icon = it.activityInfo.loadIcon(packageManager),
            iconResource = it.activityInfo.iconResource,
            banner = it.activityInfo.loadBanner(packageManager),
            packageName = it.activityInfo.packageName,
            isGame = packageIsGame(context, it.activityInfo.packageName),
            logo = it.activityInfo.loadLogo(packageManager)
        )
    }.filter { it.isGame == true }.sortedBy { it.appName.lowercase() }
}

fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
    return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
}


@ColorInt
fun Context.getColorAttr(attr: Int): Int {
    val ta = obtainStyledAttributes(intArrayOf(attr))
    @ColorInt val colorAccent = ta.getColor(0, 0)
    ta.recycle()
    return colorAccent
}

fun ImageView.tintDrawable(color: Int) {
    val drawable = drawable.mutate()
    drawable.setTint(color)
    setImageDrawable(drawable)
}

val Context.hasStoragePermission
    get() = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.READ_EXTERNAL_STORAGE
    )


val Configuration.usingNightMode get() = uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

inline infix fun Int.hasFlag(flag: Int) = (this and flag) != 0

fun ViewGroup.getAllChilds() = ArrayList<View>().also { getAllChilds(it) }

fun ViewGroup.getAllChilds(list: MutableList<View>) {
    for (i in (0 until childCount)) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.getAllChilds(list)
        } else {
            list.add(child)
        }
    }
}

fun Context.checkPackagePermission(packageName: String, permissionName: String): Boolean {
    try {
        val info = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        info.requestedPermissions.forEachIndexed { index, s ->
            if (s == permissionName) {
                return info.requestedPermissionsFlags[index].hasFlag(PackageInfo.REQUESTED_PERMISSION_GRANTED)
            }
        }
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return false
}

fun createPill(color: Int, radius: Float): Drawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(color)
        cornerRadius = radius
    }
}

fun Context.getIcon(): Drawable = packageManager.getApplicationIcon(applicationInfo)

fun dpToPx(size: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, Resources.getSystem().displayMetrics)
}

fun pxToDp(size: Float): Float {
    return size / dpToPx(1f)
}

fun <T, U : Comparable<U>> comparing(extractKey: (T) -> U): Comparator<T> {
    return Comparator { o1, o2 -> extractKey(o1).compareTo(extractKey(o2)) }
}

fun <T, U : Comparable<U>> Comparator<T>.then(extractKey: (T) -> U): Comparator<T> {
    return kotlin.Comparator { o1, o2 ->
        val res = compare(o1, o2)
        if (res != 0) res else extractKey(o1).compareTo(extractKey(o2))
    }
}

val ATLEAST_OREO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O