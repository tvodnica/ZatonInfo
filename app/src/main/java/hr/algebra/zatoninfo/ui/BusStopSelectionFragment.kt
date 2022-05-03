package hr.algebra.zatoninfo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentBusStopSelectionBinding
import hr.algebra.zatoninfo.framework.fetchItems
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.ui.adapters.BusStopItemAdapter

class BusStopSelectionFragment : Fragment() {

    private lateinit var binding: FragmentBusStopSelectionBinding
    private lateinit var pointsOfInterest: MutableList<PointOfInterest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusStopSelectionBinding.inflate(layoutInflater, container, false)
        pointsOfInterest = requireContext().fetchItems()

        loadList()
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.btnHelpChooseBusStop.setOnClickListener {
           findNavController().navigate(R.id.nav_busStopChooserToMap)
        }
    }
    private fun loadList() {
        val allBusStops = mutableListOf<PointOfInterest>()
        pointsOfInterest.forEach {
            if (it.type == getString(R.string.busStop)) {
                allBusStops.add(it)
            }
        }
        binding.rvBusStop.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BusStopItemAdapter(context, allBusStops)
        }
    }
}