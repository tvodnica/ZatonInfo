package hr.algebra.zatoninfo.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.framework.fetchItems
import hr.algebra.zatoninfo.model.PointOfInterest

class BusStopsMapFragment : Fragment() {

    private lateinit var pointsOfInterest: MutableList<PointOfInterest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bus_stops_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pointsOfInterest = requireContext().fetchItems()
        val mapFragment = childFragmentManager.findFragmentById(R.id.bus_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }



    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setMinZoomPreference(10f)
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        for (pointOfInterest in pointsOfInterest) {
            if (pointOfInterest.type == getString(R.string.busStop)) {
                val busStop = LatLng(pointOfInterest.lat, pointOfInterest.lon)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(busStop)
                        .title(pointOfInterest.name)
                        .snippet(pointOfInterest.description)
                )
            }
        }

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    42.69464173143508, 18.043565409578523
                )
            )
        )
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }
}