package hr.algebra.zatoninfo.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentMapBinding
import hr.algebra.zatoninfo.framework.fetchItems
import hr.algebra.zatoninfo.model.PointOfInterest

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private var allPointsOfInterest = mutableListOf<PointOfInterest>()
    private var poisToShowOnMap = mutableListOf<PointOfInterest>()
    private lateinit var allPoiTypes: Array<String?>
    private lateinit var checkedItems: BooleanArray
    private var allMarkers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        loadAndPrepareData()
        loadPoiTypes()
        setupFilter()
        return binding.root
    }

    private fun loadAndPrepareData() {
        allPointsOfInterest = requireContext().fetchItems()
        allPointsOfInterest.forEach {
            poisToShowOnMap.add(it)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun loadPoiTypes() {

        val allPoiTypesSet = mutableSetOf<String>()
        allPointsOfInterest.forEach {
            allPoiTypesSet.add(it.type)
        }

        var i = 0
        allPoiTypes = arrayOfNulls(allPoiTypesSet.size)
        checkedItems = BooleanArray(allPoiTypesSet.size)
        allPoiTypesSet.forEach {
            allPoiTypes[i] = it
            checkedItems[i] = true
            i++
        }
    }

    private fun setupFilter() {

        binding.btnFilter.setOnClickListener {

            AlertDialog.Builder(requireContext()).apply {
                setTitle(R.string.filter)
                setCancelable(false)
                setPositiveButton(R.string.ok) { _, _ -> updateMap() }
                setMultiChoiceItems(allPoiTypes, checkedItems) { dialog, which, isChecked ->
                    checkedItems[which] = isChecked
                }
                show()
            }

        }
    }

    private fun updateMap() {

        poisToShowOnMap.clear()

        val poiTypesToShow = mutableListOf<String>()
        for ((index, value) in allPoiTypes.withIndex()){
            if (checkedItems[index]){
                poiTypesToShow.add(value!!)
            }
        }
        allPointsOfInterest.forEach {
            if (poiTypesToShow.contains(it.type)){
                poisToShowOnMap.add(it)
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setMinZoomPreference(10f)
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        loadPoiMarkers(googleMap)

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    42.69464173143508, 18.043565409578523
                )
            )
        )
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }

    private fun loadPoiMarkers(googleMap: GoogleMap) {
        allMarkers.forEach {
            it.remove()
        }
        allMarkers.clear()
        for (poi in poisToShowOnMap) {

            val position = LatLng(poi.lat, poi.lon)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(poi.name)
                    .snippet(poi.type)
            )
            allMarkers.add(marker!!)
        }
    }



}