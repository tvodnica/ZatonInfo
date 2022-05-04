package hr.algebra.zatoninfo.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.framework.fetchItems
import hr.algebra.zatoninfo.framework.isGpsEnabled
import hr.algebra.zatoninfo.model.PointOfInterest

class BusStopsMapFragment : Fragment() {

    private lateinit var pointsOfInterest: MutableList<PointOfInterest>
    private var hasPermission = false
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkLocationPermission()
        return inflater.inflate(R.layout.fragment_bus_stops_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pointsOfInterest = requireContext().fetchItems()
        getAndLoadMap()
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

        if (hasPermission) {
            googleMap.isMyLocationEnabled = true
        }

        googleMap.setOnMyLocationButtonClickListener {
            if (!requireContext().isGpsEnabled()) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle(R.string.error)
                    setMessage(getString(R.string.noGpsErrorMessage))
                    setPositiveButton(R.string.ok, null)
                    show()
                }
            }
            false
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

    private fun getAndLoadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.bus_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            hasPermission = false
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            hasPermission = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    hasPermission = true
                    getAndLoadMap()
                } else {
                    // permission denied, boo!
                    hasPermission = false
                    Toast.makeText(
                        requireContext(),
                        "Location permission denied. You will not be able to see your location on the map.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }
}