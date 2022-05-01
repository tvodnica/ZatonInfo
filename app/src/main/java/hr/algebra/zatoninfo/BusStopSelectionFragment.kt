package hr.algebra.zatoninfo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import hr.algebra.zatoninfo.databinding.FragmentBusStopSelectionBinding

class BusStopSelectionFragment : Fragment() {

    private lateinit var binding: FragmentBusStopSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusStopSelectionBinding.inflate(layoutInflater, container, false)

        loadList()
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.btnHelpChooseBusStop.setOnClickListener {
           findNavController().navigate(R.id.busStopChooserToMap)
        }
    }
    private fun loadList() {
    }
}