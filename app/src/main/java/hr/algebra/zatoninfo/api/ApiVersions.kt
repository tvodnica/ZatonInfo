package hr.algebra.zatoninfo.api

import com.google.gson.annotations.SerializedName

class ApiVersions(

    @SerializedName("name") val name : String,
    @SerializedName("version") val version : Int

) {
}