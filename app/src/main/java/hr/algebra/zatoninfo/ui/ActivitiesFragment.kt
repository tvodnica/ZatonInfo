package hr.algebra.zatoninfo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.framework.fetchActivities
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.adapters.TripsAdapter
import hr.algebra.zatoninfo.databinding.FragmentActivitiesBinding

class ActivitiesFragment : Fragment() {

    private lateinit var binding: FragmentActivitiesBinding
    private lateinit var allActivities: MutableList<PointOfInterest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentActivitiesBinding.inflate(layoutInflater, container,false)

        loadAndPrepareData()
        loadList()

        return binding.root
    }

    private fun loadAndPrepareData() {
        allActivities = requireContext().fetchActivities()
    }

    private fun loadList() {
        binding.rvTrips.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TripsAdapter(context, allActivities)
        }
    }
}