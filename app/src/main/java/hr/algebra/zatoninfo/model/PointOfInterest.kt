package hr.algebra.zatoninfo.model

data class PointOfInterest(

    val _id: Long,
    val name: String,
    val description: String,
    val type: String,
    val lat: Double,
    val lon: Double,
    val pictures: List<String>,
    var favorite: Boolean

) {
}