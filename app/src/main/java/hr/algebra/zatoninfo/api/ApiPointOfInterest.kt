package hr.algebra.zatoninfo.api

import com.google.gson.annotations.SerializedName

data class ApiPointOfInterest (

    @SerializedName("_id") val _id : Long,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("type") val type : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lon") val lon : Double,
    @SerializedName("pictures") val pictures : String
)