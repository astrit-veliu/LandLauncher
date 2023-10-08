package io.github.astrit_veliu.landlauncher.common.utils

import android.view.View
import android.view.animation.DecelerateInterpolator

fun animateItemView(view: View, position: Int) {
    var animationDelay: Long = 500
    animationDelay += position * 30.toLong()
    view.scaleY = 0f
    view.scaleX = 0f
    view.animate()
        .scaleY(1f)
        .scaleX(1f)
        .setDuration(200)
        .setInterpolator(DecelerateInterpolator())
        .setListener(null)
        .setStartDelay(animationDelay)
        .start()
}