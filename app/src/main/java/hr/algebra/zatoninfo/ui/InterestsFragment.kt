package hr.algebra.zatoninfo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.databinding.FragmentInterestsBinding
import hr.algebra.zatoninfo.framework.fetchPoisWithoutActivities
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.adapters.InterestsAdapter

class InterestsFragment : Fragment() {

    private var allPointsOfInterest = mutableListOf<PointOfInterest>()
    private lateinit var binding: FragmentInterestsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInterestsBinding.inflate(layoutInflater, container, false)
        allPointsOfInterest = requireContext().fetchPoisWithoutActivities()
        loadList()
        return binding.root
    }

    private fun loadList() {
        val allTypesSet = mutableSetOf<String>()
        val allTypesList = mutableListOf<String>()
        allPointsOfInterest.forEach {
            allTypesSet.add(it.type)
        }
        allTypesSet.forEach{
            allTypesList.add(it)
        }
        binding.rvInterests.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = InterestsAdapter(context, allTypesList)
        }

    }
}