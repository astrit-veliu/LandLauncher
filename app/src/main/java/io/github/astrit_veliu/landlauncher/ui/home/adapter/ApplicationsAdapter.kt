package io.github.astrit_veliu.landlauncher.ui.home.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.astrit_veliu.landlauncher.domain.model.Application
import io.github.astrit_veliu.landlauncher.ui.common.view.MenuApp

class ApplicationsAdapter(
    applications: List<Application>,
    context: Context
) : RecyclerView.Adapter<ApplicationsAdapter.ViewHolder>() {

    private var onApplicationClick: ((application: Application?) -> Unit)? = null
    private val context: Context
    private var items: List<Application> = emptyList()

    init {
        items = applications
        this.context = context
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = items[position]
        viewHolder.bind(data, onApplicationClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MenuApp(context))
    }

    class ViewHolder constructor(itemView: MenuApp) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnFocusChangeListener { _, hasFocus -> itemView.handleFocus(hasFocus) }
        }

        fun bind(application: Application, onApplicationClick: ((application: Application?) -> Unit)?) {
            val view = itemView as MenuApp
            view.setApplication(application)
            view.setOnAppClick { onApplicationClick?.invoke(it) }
        }

    }

    override fun getItemCount() = items.size

    fun setOnApplicationClick(onApplicationClick: ((application: Application?) -> Unit)?) {
        this.onApplicationClick = onApplicationClick
    }
}