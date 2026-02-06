package cu.axel.smartdock.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cu.axel.smartdock.R

data class SettingCategory(val id: Int, val title: String, val iconRes: Int, val fragmentClass: Class<*>)

class SettingsSidebarAdapter(
    private val categories: List<SettingCategory>,
    private val onCategorySelected: (SettingCategory) -> Unit
) : RecyclerView.Adapter<SettingsSidebarAdapter.ViewHolder>() {

    private var selectedPosition = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.category_icon)
        val title: TextView = itemView.findViewById(R.id.category_title)

        fun bind(category: SettingCategory, position: Int) {
            title.text = category.title
            icon.setImageResource(category.iconRes)

            if (position == selectedPosition) {
                itemView.setBackgroundColor(Color.parseColor("#E5E5E5")) // Selected highlight
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            itemView.setOnClickListener {
                val prev = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(prev)
                notifyItemChanged(selectedPosition)
                onCategorySelected(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position], position)
    }

    override fun getItemCount() = categories.size
}
