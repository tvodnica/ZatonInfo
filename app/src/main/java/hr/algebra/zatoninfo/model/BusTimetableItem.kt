package hr.algebra.zatoninfo.model

data class BusTimetableItem(

    val busNumber: String,
    val time: String,
    val notice: String,
    val direction: String,
    val busStop: String

) {
}