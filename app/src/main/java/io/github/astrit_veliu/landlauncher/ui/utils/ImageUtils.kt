package io.github.astrit_veliu.landlauncher.ui.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Drawable.getBitmapFromDrawable(): Bitmap? {
    val image = this
    return if (image is BitmapDrawable)
        image.bitmap
    else {
        var iWidth = image.intrinsicWidth
        val iHeight = image.intrinsicHeight
        if (iWidth < 0)
            iWidth = 1
        if (iHeight < 0)
            iHeight < 1
        val bmp = Bitmap.createBitmap(iWidth, iHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        image.setBounds(0, 0, canvas.width, canvas.height)
        image.draw(canvas)
        bmp
    }
}
