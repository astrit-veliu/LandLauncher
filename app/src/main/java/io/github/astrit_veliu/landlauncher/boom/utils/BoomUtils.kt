package io.github.astrit_veliu.landlauncher.boom.utils

import android.os.Build.VERSION_CODES
import android.view.View
import io.github.astrit_veliu.landlauncher.boom.Boom

object BoomUtils {

    fun boomAll(vararg views: View) {
        views.forEach { Boom(it) }
    }

}

fun View.boom(withRipple: Boolean? = false) {
    Boom(this)
    if (withRipple == true && android.os.Build.VERSION.SDK_INT >= VERSION_CODES.M) this.applyRipple()
}

fun View.removeBoom() {
    this.setOnTouchListener(null)
}