package io.github.astrit_veliu.landlauncher.domain.model

import android.graphics.drawable.Drawable
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory.OTHERS

data class Application(
    val appName: String,
    val icon: Drawable,
    val banner: Drawable? = null,
    val packageName: String,
    val category: ApplicationCategory? = OTHERS,
    val isSystemApp: Boolean? = true,
    val logo: Any? = null
) {

    enum class ApplicationCategory constructor(val value: String) {
        GAME("game"),
        MEDIA("media"),
        SOCIAL("social"),
        UTILS("utils"),
        OTHERS("others")
    }
}