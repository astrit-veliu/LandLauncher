package io.github.astrit_veliu.landlauncher.ui.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.boom.Boom
import io.github.astrit_veliu.landlauncher.data.ApplicationEntity
import io.github.astrit_veliu.landlauncher.databinding.DrawerApplicationViewBinding

class DrawerApp @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = DrawerApplicationViewBinding.inflate(LayoutInflater.from(context), this)

    private var onApplicationClick: ((application: ApplicationEntity?) -> Unit)? = null
    private var applicationEntity: ApplicationEntity? = null

    init {

        Boom(binding.applicationIcon)
        binding.applicationIcon.setOnFocusChangeListener { view, hasFocus ->
            isFocusable = hasFocus
        }
        binding.rootContainer.setOnFocusChangeListener { view, hasFocus ->
            handleFocus(hasFocus)
        }
        binding.applicationIcon.setOnClickListener { onApplicationClick?.invoke(applicationEntity) }
        binding.rootContainer.setOnClickListener { onApplicationClick?.invoke(applicationEntity) }
        //setOnClickListener { onApplicationClick?.invoke(applicationEntity) }
    }

    fun setApplication(application: ApplicationEntity?) {
        this.applicationEntity = application
        setIcon(application?.icon)
    }

    fun setIcon(icon: Drawable?, circleCrop: Boolean? = false) {
        Glide.with(this.context).load(icon).let { request ->
            if (circleCrop == true) request.circleCrop()
            request.into(binding.applicationIcon)
        }
    }

    fun setOnApplicationClick(onApplicationClick: ((application: ApplicationEntity?) -> Unit)?) {
        this.onApplicationClick = onApplicationClick
    }

    private fun handleFocus(hasFocus: Boolean) {
        val anim = AnimationUtils.loadAnimation(context, if (hasFocus) R.anim.scale_in else R.anim.scale_out)
        binding.applicationIcon.startAnimation(anim)
        anim.fillAfter = true
    }
}