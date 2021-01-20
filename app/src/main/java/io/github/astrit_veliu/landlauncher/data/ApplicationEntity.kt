package io.github.astrit_veliu.landlauncher.data

import android.graphics.drawable.Drawable

data class ApplicationEntity(
        val appName: String,
        val icon: Drawable,
        val banner: Drawable?,
        val packageName: String
)