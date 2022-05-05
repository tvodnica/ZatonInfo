package hr.algebra.zatoninfo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentTripsBinding
import hr.algebra.zatoninfo.framework.fetchItems
import hr.algebra.zatoninfo.framework.fetchTrips
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.ui.adapters.SpecificInterestAdapter
import hr.algebra.zatoninfo.ui.adapters.TripsAdapter

class TripsFragment : Fragment() {

    private lateinit var binding: FragmentTripsBinding
    private lateinit var allTrips: MutableList<PointOfInterest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTripsBinding.inflate(layoutInflater, container,false)

        loadAndPrepareData()
        loadList()

        return binding.root
    }

    private fun loadAndPrepareData() {
        allTrips = requireContext().fetchTrips()
    }

    private fun loadList() {
        binding.rvTrips.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TripsAdapter(context, allTrips)
        }
    }
}