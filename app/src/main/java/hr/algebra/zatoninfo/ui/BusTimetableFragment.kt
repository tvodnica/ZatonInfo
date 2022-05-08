package hr.algebra.zatoninfo.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentBusTimetableBinding
import hr.algebra.zatoninfo.framework.fetchBusTimetable
import hr.algebra.zatoninfo.model.BusTimetableItem
import hr.algebra.zatoninfo.adapters.BusTimetableAdapter

class BusTimetableFragment(private val startingDestination: Boolean) : Fragment() {

    private lateinit var binding: FragmentBusTimetableBinding
    private var busTimetable = mutableListOf<BusTimetableItem>()
    private var selectedBusStopTimetable = mutableListOf<BusTimetableItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusTimetableBinding.inflate(layoutInflater, container, false)
        loadAndPrepareData()
        loadList()
        return binding.root
    }

    private fun loadAndPrepareData() {

        busTimetable = requireContext().fetchBusTimetable()

        val selectedBusStopName = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(getString(R.string.selectedBusStopName), "")
        val selectedBusStopDirection = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(getString(R.string.selectedBusStopDirection), "")

        busTimetable.forEach {
            if (it.busStop == selectedBusStopName && it.direction == selectedBusStopDirection){
                selectedBusStopTimetable.add(it)
            }
        }

        if(startingDestination){
            selectedBusStopTimetable.clear()
            busTimetable.forEach {
                if (it.busStop == getString(R.string.startingPoint) && it.direction == selectedBusStopDirection){
                    selectedBusStopTimetable.add(it)
                }
            }
        }

    }

    private fun loadList() {
        binding.rvBusTimetable.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BusTimetableAdapter(requireContext(), selectedBusStopTimetable)
        }
    }

}
