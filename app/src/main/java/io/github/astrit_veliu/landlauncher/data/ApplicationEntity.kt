package io.github.astrit_veliu.landlauncher.data

import android.graphics.drawable.Drawable

data class ApplicationEntity(
    val appName: String,
    val icon: Drawable,
    val iconResource: Int,
    val banner: Drawable? = null,
    val packageName: String,
    val isGame: Boolean? = false,
    val logo: Any? = null
)