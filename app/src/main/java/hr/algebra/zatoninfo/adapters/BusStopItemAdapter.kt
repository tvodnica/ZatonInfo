package hr.algebra.zatoninfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.model.PointOfInterest

class BusStopItemAdapter(
    private val context: Context,
    private val items: MutableList<PointOfInterest>
) : RecyclerView.Adapter<BusStopItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv_busStop = itemView.findViewById<TextView>(R.id.tv_busStop)
        private val tv_busStopDirection = itemView.findViewById<TextView>(R.id.tv_busStopDirection)
        fun bind(item: PointOfInterest) {
            tv_busStop.text = item.name
            tv_busStopDirection.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        itemView = LayoutInflater
            .from(parent.context).inflate(R.layout.bus_stop_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(
                            R.string.selectedBusStopDirection), items[position].description).apply()
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(
                            R.string.selectedBusStopName), items[position].name).apply()
            Navigation.findNavController(it).navigate(R.id.nav_busStopChooserToTimetable)
        }
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size
}