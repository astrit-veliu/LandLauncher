package io.github.astrit_veliu.landlauncher.ui.common.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.databinding.RecommendationsItemBinding
import io.github.astrit_veliu.landlauncher.domain.model.Application

class HomeItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = RecommendationsItemBinding.inflate(LayoutInflater.from(context), this)

    private var onApplicationClick: ((application: Application?) -> Unit)? = null
    var applicationEntity: Application? = null

    init {
        background = ContextCompat.getDrawable(context, R.drawable.home_app_selector)
        isFocusable = true
        isFocusableInTouchMode = true
        layoutParams = LayoutParams(400, LayoutParams.MATCH_PARENT)
        binding.root.setOnClickListener { onApplicationClick?.invoke(applicationEntity) }
    }

    fun setApplication(application: Application?) {
        this.applicationEntity = application
        application?.let {
            val icon = it.icon
            val banner = it.banner
            val palette = Palette.from(icon.toBitmap()).generate()
            binding.applicationCard.setCardBackgroundColor(palette.getDominantColor(Color.BLACK))
            binding.logoImage.setImageDrawable(icon)
            binding.root.setOnClickListener { onApplicationClick?.invoke(application) }
        }
    }

    fun setOnAppClick(onApplicationClick: ((application: Application?) -> Unit)?) {
        this.onApplicationClick = onApplicationClick
    }

    fun showStroke(show: Boolean) {
        binding.applicationCard.strokeColor = ContextCompat.getColor(context, if (show) R.color.materialBlue else R.color.transparent)
        binding.applicationCard.strokeWidth = if (show)4 else 0
    }

    fun showHoverView(show: Boolean) {
         binding.hoverView.isVisible = show
    }
}