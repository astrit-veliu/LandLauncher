package io.github.astrit_veliu.landlauncher.ui.home.adapter

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.domain.model.Application
import io.github.astrit_veliu.landlauncher.ui.common.view.HomeItem

class RecommendationsAdapter(
    applications: List<Application>,
    context: Context
) : RecyclerView.Adapter<RecommendationsAdapter.ViewHolder>() {

    private var onApplicationClick: ((application: Application?) -> Unit)? = null
    private var onApplicationFocused: ((application: Application?) -> Unit)? = null
    private val context: Context
    var items: List<Application> = emptyList()

    init {
        items = applications
        this.context = context
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = items[position]
        viewHolder.bind(data, onApplicationClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HomeItem(context), context, onApplicationFocused)
    }

    class ViewHolder constructor(itemView: HomeItem, context: Context, onApplicationFocused: ((application: Application?) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnFocusChangeListener { view, hasFocus ->
                val anim = AnimationUtils.loadAnimation(context, if (hasFocus) R.anim.scale_in else R.anim.scale_out)
                view.startAnimation(anim)
                anim.fillAfter = true
                itemView.showStroke(hasFocus)
                if (hasFocus) onApplicationFocused?.invoke(itemView.applicationEntity)
            }
            itemView.setOnHoverListener { view, motionEvent ->
                val showPlaceholder = when (motionEvent.action) {
                    MotionEvent.ACTION_HOVER_ENTER -> true
                    MotionEvent.ACTION_HOVER_EXIT -> false
                    else -> true
                }
                itemView.showHoverView(showPlaceholder)
                return@setOnHoverListener false
            }
        }

        fun bind(application: Application, onApplicationClick: ((application: Application?) -> Unit)?) {
            (itemView as HomeItem).apply {
                setApplication(application)
                setOnAppClick { onApplicationClick?.invoke(it) }
            }
        }
    }

    override fun getItemCount() = items.size

    fun setOnApplicationClick(onApplicationClick: ((application: Application?) -> Unit)?) {
        this.onApplicationClick = onApplicationClick
    }

    fun setOnApplicationFocused(onApplicationFocused: ((application: Application?) -> Unit)?) {
        this.onApplicationFocused = onApplicationFocused
    }
}