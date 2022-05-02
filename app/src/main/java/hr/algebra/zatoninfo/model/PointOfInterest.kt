package hr.algebra.zatoninfo.model

import com.google.android.gms.maps.model.LatLng

class PointOfInterest(

    val _id: Long,
    val name: String,
    val description: String,
    val type: String,
    val lat: Double,
    val lon: Double,
    val pictures: String,
    val favorite: Boolean

) {
}