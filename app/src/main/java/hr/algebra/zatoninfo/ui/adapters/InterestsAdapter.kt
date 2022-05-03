package hr.algebra.zatoninfo.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentViewHolder
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.model.PointOfInterest

class InterestsAdapter(
    private val context: Context,
    private val items: MutableList<String>
) : RecyclerView.Adapter<InterestsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv_interest = itemView.findViewById<TextView>(R.id.tv_interest)
        fun bind(typeName: String) {
            tv_interest.text = typeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        itemView = LayoutInflater
            .from(parent.context).inflate(R.layout.interests_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.nav_allInterestsToSpecificOne)
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.interestType), items[position])
                .apply()
        }
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}