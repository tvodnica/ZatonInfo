package hr.algebra.zatoninfo.api

import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://pastebin.com/raw/"

interface ZatonApi {
    @GET("8bjPwxkL")
    fun fetchItems() : Call<List<ApiPointOfInterest>>
}