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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }



    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

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