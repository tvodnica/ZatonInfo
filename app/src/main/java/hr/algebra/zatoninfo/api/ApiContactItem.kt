package hr.algebra.zatoninfo.api

import com.google.gson.annotations.SerializedName

data class ApiContactItem (

    @SerializedName("message") val message : String

)