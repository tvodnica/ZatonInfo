package hr.algebra.zatoninfo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentSpecificInterestsBinding
import hr.algebra.zatoninfo.framework.fetchPoisWithoutActivities
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.adapters.SpecificInterestAdapter

class SpecificInterestsFragment : Fragment() {

    private var allPointsOfInterest = mutableListOf<PointOfInterest>()
    private lateinit var binding: FragmentSpecificInterestsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpecificInterestsBinding.inflate(layoutInflater, container, false)
        loadList()
        return binding.root
    }

    private fun loadList() {
        val interestType = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString(getString(R.string.interestType), getString(R.string.error))

        val selectedPois = mutableListOf<PointOfInterest>()
        allPointsOfInterest = requireContext().fetchPoisWithoutActivities()

        for (poi in allPointsOfInterest) {
            if (poi.type == interestType) {
                selectedPois.add(poi)
            }
        }

        binding.rvSpecificInterests.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SpecificInterestAdapter(context, selectedPois)
        }
    }


}