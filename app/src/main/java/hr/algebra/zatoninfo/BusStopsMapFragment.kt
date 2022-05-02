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
import hr.algebra.zatoninfo.api.ZatonFetcher
import hr.algebra.zatoninfo.model.PointOfInterest

class BusStopsMapFragment : Fragment() {

    private val pointsOfInterest = mutableListOf<PointOfInterest>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bus_stops_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchItems()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun fetchItems() {
        val cursor =
            requireContext().contentResolver.query(ZATON_PROVIDER_URI, null, null, null, null)
        while (cursor != null && cursor.moveToNext()) {
            pointsOfInterest.add(
                PointOfInterest(
                    cursor.getLong(cursor.getColumnIndexOrThrow(PointOfInterest::_id.name)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::name.name)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::description.name)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::type.name)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(PointOfInterest::lat.name)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(PointOfInterest::lon.name)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PointOfInterest::pictures.name)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PointOfInterest::favorite.name)) == 1
                )
            )
        }
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
                    MarkerOptions().position(busStop).title(pointOfInterest.name)
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