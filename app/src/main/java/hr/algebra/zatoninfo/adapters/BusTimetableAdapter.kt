package hr.algebra.zatoninfo.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.model.BusTimetableItem


class BusTimetableAdapter(
    private val context: Context,
    private val items: MutableList<BusTimetableItem>
) : RecyclerView.Adapter<BusTimetableAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val weight_zero = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            0f
        )

        val weight_one = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            1f
        )

        private val tv_busNumber = itemView.findViewById<TextView>(R.id.tv_busNumber)
        private val tv_busTime = itemView.findViewById<TextView>(R.id.tv_busTime)
        private val tv_busNotice = itemView.findViewById<TextView>(R.id.tv_busNotice)

        fun bind(item: BusTimetableItem) {
            tv_busNumber.text = item.busNumber
            tv_busTime.text = item.time
            tv_busNotice.text = item.notice

            if (item.busNumber == "" && item.time == ""){
                tv_busNumber.layoutParams = weight_zero
                tv_busTime.layoutParams = weight_zero
            }
            else{
                tv_busNumber.layoutParams = weight_one
                tv_busTime.layoutParams = weight_one
            }
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