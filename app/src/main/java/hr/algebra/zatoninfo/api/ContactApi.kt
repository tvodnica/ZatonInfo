package hr.algebra.zatoninfo.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

const val CONTACT_API_URL = "https://formspree.io/f/"

interface ContactApi {

    @POST("xjvlzgga")
    fun sendMessage(@Body apiContactItem: ApiContactItem): Call<Unit>
}