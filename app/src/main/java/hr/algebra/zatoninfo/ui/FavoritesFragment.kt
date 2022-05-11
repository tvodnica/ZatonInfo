package hr.algebra.zatoninfo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.zatoninfo.databinding.FragmentFavoritesBinding
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.adapters.SpecificInterestAdapter
import hr.algebra.zatoninfo.framework.fetchAllPointsOfInterest

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        loadList()
        return binding.root
    }

    private fun loadList() {
        val allPointsOfInterest = requireContext().fetchAllPointsOfInterest()
        val favoritePois = mutableListOf<PointOfInterest>()
        allPointsOfInterest.forEach {
            if (it.favorite) {
                favoritePois.add(it)
            }
        }

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SpecificInterestAdapter(context, favoritePois)
        }
    }
}