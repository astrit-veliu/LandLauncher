package io.github.astrit_veliu.landlauncher.ui.common

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.databinding.BindingAdapter
import io.github.astrit_veliu.landlauncher.ui.common.view.DrawerApp

@BindingAdapter("applicationIcon")
fun DrawerApp.setAppIcon(iconDrawable: Drawable?) {
    Log.d("IMAZHI:",iconDrawable.toString())
    setIcon(iconDrawable)
}

@SuppressLint("CheckResult")
@BindingAdapter(
    "imageSrcUrl",
    "circleImage",
    requireAll = false // makes the attributes optional
)
fun DrawerApp.bindImage(
    iconDrawable: Drawable?,
    circleCrop: Boolean = false,
) {
    setIcon(iconDrawable, circleCrop)
}