package hr.algebra.zatoninfo.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.framework.fetchItems
import hr.algebra.zatoninfo.model.PointOfInterest

class MapFragment : Fragment() {

    private var allPointsOfInterest = mutableListOf<PointOfInterest>()

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setMinZoomPreference(10f)
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        for (poi in allPointsOfInterest) {

            val position = LatLng(poi.lat, poi.lon)
            googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(poi.name)
                    .snippet(poi.type)
            )
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allPointsOfInterest = requireContext().fetchItems()
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}