package hr.algebra.zatoninfo.api

import hr.algebra.zatoninfo.ui.BusStopItemAdapter
import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://pastebin.com/raw/"

interface ZatonApi {
    @GET("8bjPwxkL")
    fun fetchItems() : Call<List<ApiPointOfInterest>>

    @GET("Uyq0wn19")
    fun fetchBusTimetable() : Call<List<ApiBusTimetableItem>>
}