package io.github.astrit_veliu.landlauncher.ui.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.databinding.ApplicationItemBinding
import io.github.astrit_veliu.landlauncher.domain.model.Application

class MenuApp @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding = ApplicationItemBinding.inflate(LayoutInflater.from(context), this)

    var onApplicationClick: ((application: Application?) -> Unit)? = null
    private var applicationEntity: Application? = null

    init {
        background = ContextCompat.getDrawable(context, R.drawable.menu_app_selector)
        gravity = Gravity.CENTER_VERTICAL
        isFocusable = true
        binding.root.setOnClickListener { onApplicationClick?.invoke(applicationEntity) }
    }

    fun setApplication(application: Application?) {
        this.applicationEntity = application
        application?.let {
            val icon = it.icon
            binding.logoImage.setImageDrawable(icon)
            binding.nameTextView.text = it.appName
        }
    }

    fun setOnAppClick(onApplicationClick: ((application: Application?) -> Unit)?) {
        this.onApplicationClick = onApplicationClick
    }

    fun handleFocus(hasFocus: Boolean) {
        val anim = AnimationUtils.loadAnimation(context, if (hasFocus) R.anim.scale_in else R.anim.scale_out)
        binding.logoImage.startAnimation(anim)
        anim.fillAfter = true
        binding.nameTextView.setTypeface(null, if (hasFocus) Typeface.BOLD else Typeface.NORMAL)
    }
}