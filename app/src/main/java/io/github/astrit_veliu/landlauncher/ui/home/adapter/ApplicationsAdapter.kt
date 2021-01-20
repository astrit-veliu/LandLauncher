package io.github.astrit_veliu.landlauncher.ui.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.data.ApplicationEntity
import kotlinx.android.synthetic.main.application_item.view.*

class ApplicationsAdapter(
    applications : List<ApplicationEntity>
) : RecyclerView.Adapter<ApplicationsAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = items[position]
        val pm = viewHolder.icon.context.packageManager

        val icon = data.icon
        val banner = data.banner
        val palette = Palette.from(icon.toBitmap()).generate()

        viewHolder.mainCardView.setCardBackgroundColor(palette.getDominantColor(Color.GRAY))
        viewHolder.icon.setImageDrawable(icon)
        viewHolder.banner.setImageDrawable(banner)
        viewHolder.name.text = data.appName
        viewHolder.packageName.text = data.packageName
        viewHolder.root.setOnClickListener {
            val launchIntent = pm.getLaunchIntentForPackage(data.packageName)
            viewHolder.root.context.startActivity(launchIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.application_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(val root: View): RecyclerView.ViewHolder(root) {
        val icon: ImageView = root.logoImage
        val banner: ImageView = root.bannerImage
        val mainCardView: MaterialCardView = root.applicationCard
        val name: TextView = root.nameTextView
        val packageName: TextView = root.packageTextView
    }

    companion object {
        var items : List<ApplicationEntity> = emptyList()
    }

    init {
        items = applications
    }
}