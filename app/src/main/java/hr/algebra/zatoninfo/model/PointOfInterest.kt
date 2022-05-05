package hr.algebra.zatoninfo.model

import java.io.File

class PointOfInterest(

    val _id: Long,
    val name: String,
    val description: String,
    val type: String,
    val lat: Double,
    val lon: Double,
    val pictures: List<String>,
    val favorite: Boolean

) {
}