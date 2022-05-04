package hr.algebra.zatoninfo.api

import com.google.gson.annotations.SerializedName

data class ApiBusTimetableItem (

    @SerializedName("number") val busNumber : String,
    @SerializedName("time") val time : String,
    @SerializedName("notice") val notice : String,
    @SerializedName("direction") val direction : String,
    @SerializedName("busStop") val busStop : String

)