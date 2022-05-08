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

class TripsAdapter(
    private val context: Context,
    private val items: MutableList<PointOfInterest>
) : RecyclerView.Adapter<TripsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv_interest = itemView.findViewById<TextView>(R.id.tv_interest)
        fun bind(item: PointOfInterest) {
            tv_interest.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        itemView = LayoutInflater
            .from(parent.context).inflate(R.layout.interests_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(context.getString(R.string.selectedInterest), items[position]._id).apply()
            Navigation.findNavController(it).navigate(R.id.nav_tripsToPoiDetails)
        }
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size
}