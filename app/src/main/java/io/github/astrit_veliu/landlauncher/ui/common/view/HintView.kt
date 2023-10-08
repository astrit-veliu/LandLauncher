package io.github.astrit_veliu.landlauncher.ui.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.github.astrit_veliu.landlauncher.databinding.HintIconsViewBinding

class HintView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    //todo make icons clickable (perform select, goBack)
    private val binding = HintIconsViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL and Gravity.END
    }

    fun setTint(color: Int?) {

    }
}