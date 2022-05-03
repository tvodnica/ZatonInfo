package hr.algebra.zatoninfo.ui

import android.os.Bundle
import android.system.Os
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.provider.FontsContractCompat
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.BUS_PROVIDER_URI
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.api.ZatonFetcher
import hr.algebra.zatoninfo.databinding.FragmentBusStopSelectionBinding
import hr.algebra.zatoninfo.databinding.FragmentBusTimetableBinding
import hr.algebra.zatoninfo.framework.fetchBusTimetable
import hr.algebra.zatoninfo.framework.fetchItems
import hr.algebra.zatoninfo.model.BusTimetableItem

class BusTimetableFragment : Fragment() {

    private lateinit var binding: FragmentBusTimetableBinding
    private var busTimetable = mutableListOf<BusTimetableItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusTimetableBinding.inflate(layoutInflater, container, false)
        busTimetable = requireContext().fetchBusTimetable()
        loadList()
        return binding.root
    }

    private fun loadList() {
        binding.rvBusTimetable.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = BusTimetableAdapter(requireContext(), busTimetable)
        }
    }

}
