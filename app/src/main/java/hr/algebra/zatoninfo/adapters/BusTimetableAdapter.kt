package hr.algebra.zatoninfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.model.BusTimetableItem


class BusTimetableAdapter(
    private val context: Context,
    private val items: MutableList<BusTimetableItem>
) : RecyclerView.Adapter<BusTimetableAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv_busNumber = itemView.findViewById<TextView>(R.id.tv_busNumber)
        private val tv_busTime = itemView.findViewById<TextView>(R.id.tv_busTime)
        private val tv_busNotice = itemView.findViewById<TextView>(R.id.tv_busNotice)

        fun bind(item: BusTimetableItem) {
            tv_busNumber.text = item.busNumber
            tv_busTime.text = item.time
            tv_busNotice.text = item.notice
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        itemView = LayoutInflater
            .from(parent.context).inflate(R.layout.bus_timetable_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size


}