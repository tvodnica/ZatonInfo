package hr.algebra.zatoninfo.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentBusTimetableBinding
import hr.algebra.zatoninfo.framework.fetchBusTimetable
import hr.algebra.zatoninfo.model.BusTimetableItem
import hr.algebra.zatoninfo.ui.adapters.BusTimetableAdapter

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
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.fabInfo.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(context.getString(R.string.busInformation))
                setMessage(context.getString(R.string.allBusDestinationsInfo))
                setPositiveButton(context.getString(R.string.ok), null)
                show()
            }
        }
    }

    private fun loadList() {
        binding.rvBusTimetable.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = BusTimetableAdapter(requireContext(), busTimetable)
        }
    }

}
