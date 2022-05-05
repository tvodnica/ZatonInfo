package hr.algebra.zatoninfo.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.BUS_PROVIDER_URI
import hr.algebra.zatoninfo.ZATON_PROVIDER_URI
import hr.algebra.zatoninfo.handler.downloadImageAndStore
import hr.algebra.zatoninfo.model.BusTimetableItem
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.ui.BUS_DATA_EXISTS
import hr.algebra.zatoninfo.ui.POI_DATA_EXISTS
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

    fun fetchBusTimetable() {
        val request = zatonApi.fetchBusTimetable()
        request.enqueue(object : Callback<List<ApiBusTimetableItem>> {
            override fun onResponse(
                call: Call<List<ApiBusTimetableItem>>,
                response: Response<List<ApiBusTimetableItem>>
            ) {
                response.body()?.let {
                    populateBusItems(it)
                }
            }

            override fun onFailure(call: Call<List<ApiBusTimetableItem>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateBusItems(it1: List<ApiBusTimetableItem>) {
        GlobalScope.launch {
            context.contentResolver.delete(BUS_PROVIDER_URI, null, null)
            it1.forEach {
                val values = ContentValues().apply {
                    put(BusTimetableItem::busNumber.name, it.busNumber)
                    put(BusTimetableItem::time.name, it.time)
                    put(BusTimetableItem::notice.name, it.notice)
                    put(BusTimetableItem::direction.name, it.direction)
                    put(BusTimetableItem::busStop.name, it.busStop)
                }
                context.contentResolver.insert(BUS_PROVIDER_URI, values)
            }
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(BUS_DATA_EXISTS, true)
                .apply()
        }
    }

    private fun populateItems(apiPointsOfInterest: List<ApiPointOfInterest>) {
        GlobalScope.launch {
            context.contentResolver.delete(ZATON_PROVIDER_URI, null, null)
            apiPointsOfInterest.forEach {

                var localPicturesPaths: String = ""
                var i: Int = 1

                it.picturesPaths.split("\n").forEach { picturePath ->
                    localPicturesPaths += downloadImageAndStore(context, picturePath, it.name + i)
                    localPicturesPaths += "\n"
                    i++
                }

                val values = ContentValues().apply {
                    put(PointOfInterest::name.name, it.name)
                    put(PointOfInterest::description.name, it.description)
                    put(PointOfInterest::type.name, it.type)
                    put(PointOfInterest::lat.name, it.lat)
                    put(PointOfInterest::lon.name, it.lon)
                    put(PointOfInterest::pictures.name, localPicturesPaths)
                    put(PointOfInterest::favorite.name, false)
                }
                context.contentResolver.insert(ZATON_PROVIDER_URI, values)
                i = 1
            }
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(POI_DATA_EXISTS, true)
                .apply()
        }
    }

}