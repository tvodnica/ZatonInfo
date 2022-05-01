package hr.algebra.zatoninfo

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.PointerIcon
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.ui.NavigationUI

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import hr.algebra.zatoninfo.model.PointOfInterest

class BusStopsMapFragment : Fragment() {

    private val pointOfInterests = mutableListOf<PointOfInterest>()

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

        pointOfInterests.add(PointOfInterest("Bunica", "", "busStop", 42.696831439426454, 18.04627282636609))

        for (pointOfInterest in pointOfInterests) {
            if (pointOfInterest.Type == "busStop") {
                val busStop = LatLng(pointOfInterest.Lat, pointOfInterest.Lon)
                googleMap.addMarker(
                    MarkerOptions().position(busStop).title(pointOfInterest.Name)
                    )

            }
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(42.696831439426454, 18.04627282636609)))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))


    /*    val bunica2 = LatLng(42.696831439426454, 18.04627282636609)
        googleMap.addMarker(
            MarkerOptions().position(bunica2).title(getString(R.string.zaton_mali_bunica))
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bunica2))

        val babilon = LatLng(42.701630513611775, 18.04192514395971)
        googleMap.addMarker(
            MarkerOptions().position(babilon).title(getString(R.string.zaton_mali_babilon))
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(babilon))*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bus_stops_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }
}