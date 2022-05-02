package hr.algebra.zatoninfo.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.DATA_EXISTS
import hr.algebra.zatoninfo.SplashScreenActivity
import hr.algebra.zatoninfo.ZATON_PROVIDER_URI
import hr.algebra.zatoninfo.model.PointOfInterest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ZatonFetcher(private val context: Context) {

    private var zatonApi: ZatonApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        zatonApi = retrofit.create(ZatonApi::class.java)
    }

    fun fetchItems() {
        val request = zatonApi.fetchItems()
        request.enqueue(object : Callback<List<ApiPointOfInterest>> {
            override fun onResponse(
                call: Call<List<ApiPointOfInterest>>,
                response: Response<List<ApiPointOfInterest>>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<List<ApiPointOfInterest>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(apiPointsOfInterest: List<ApiPointOfInterest>) {
        GlobalScope.launch {
            apiPointsOfInterest.forEach {

                val values = ContentValues().apply {
                    put(PointOfInterest::name.name, it.name)
                    put(PointOfInterest::description.name, it.description)
                    put(PointOfInterest::type.name, it.type)
                    put(PointOfInterest::lat.name, it.lat)
                    put(PointOfInterest::lon.name, it.lon)
                    put(PointOfInterest::pictures.name, it.pictures)
                    put(PointOfInterest::favorite.name, false)
                }
                context.contentResolver.insert(ZATON_PROVIDER_URI, values)
            }
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(DATA_EXISTS, true)
                .apply()
        }
    }
}