package io.github.astrit_veliu.landlauncher.ui.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import io.github.astrit_veliu.landlauncher.data.ApplicationEntity
import io.github.astrit_veliu.landlauncher.databinding.RecommendationsItemBinding

class HomeAppsAdapter(
    private val items: List<ApplicationEntity>
) : RecyclerView.Adapter<HomeAppsAdapter.ViewHolder>() {

    lateinit var appBinding: RecommendationsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        appBinding = RecommendationsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(appBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        val pm = holder.itemLayoutBinding.logoImage.context.packageManager

        val icon = data.icon
        val banner = data.banner
        val palette = Palette.from(icon.toBitmap()).generate()

        holder.itemLayoutBinding.applicationCard.setCardBackgroundColor(palette.getDominantColor(Color.GRAY))
        holder.itemLayoutBinding.logoImage.setImageDrawable(icon)
        holder.itemLayoutBinding.root.setOnClickListener {
            val launchIntent = pm.getLaunchIntentForPackage(data.packageName)
            holder.itemLayoutBinding.root.context.startActivity(launchIntent)
        }

    }

    class ViewHolder(val itemLayoutBinding: RecommendationsItemBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root)
}