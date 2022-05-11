package hr.algebra.zatoninfo.api

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.BUS_PROVIDER_URI
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.ZATON_PROVIDER_URI
import hr.algebra.zatoninfo.ZatonReceiver
import hr.algebra.zatoninfo.framework.fetchAllPointsOfInterest
import hr.algebra.zatoninfo.framework.getPreferences
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
import java.io.File

const val POI_VERSION = "hr.algebra.zatoninfo.poi_version"
const val BUS_VERSION = "hr.algebra.zatoninfo.bus_version"

class ZatonFetcher(private val context: Context) {

    private var zatonApi: ZatonApi
    private val prefs = context.getPreferences()
    var newPoiVersionAvailable = true
    var newBusVersionAvailable = true
    var finishedCheckingVersion = false


    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        zatonApi = retrofit.create(ZatonApi::class.java)

        prefs.edit()
            .putBoolean(POI_DATA_EXISTS, false)
            .putBoolean(BUS_DATA_EXISTS, false)
            .apply()

        fetchApiVersions()
    }

    private fun fetchApiVersions() {

        val request = zatonApi.fetchVersions()
        request.enqueue(object : Callback<List<ApiVersions>> {
            override fun onResponse(
                call: Call<List<ApiVersions>>,
                response: Response<List<ApiVersions>>
            ) {
                response.body()?.let { list ->

                    list.forEach {

                        if (it.name == context.getString(R.string.pointsOfInterest)) {
                            if (it.version == prefs.getInt(
                                    POI_VERSION, 0
                                )
                            ) {
                                newPoiVersionAvailable = false
                            }
                            prefs.edit().putInt(POI_VERSION, it.version)
                                .apply()
                        }

                        if (it.name == context.getString(R.string.busTimetable)) {
                            if (it.version == prefs.getInt(
                                    BUS_VERSION,
                                    0
                                )
                            ) {
                                newBusVersionAvailable = false
                            }
                            prefs.edit().putInt(BUS_VERSION, it.version)
                                .apply()
                        }
                    }
                }
                finishedCheckingVersion = true

            }

            override fun onFailure(call: Call<List<ApiVersions>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    fun fetchPois() {

        while (!finishedCheckingVersion) {

        }

        if (!newPoiVersionAvailable) {
            prefs.edit()
                .putBoolean(POI_DATA_EXISTS, true)
                .apply()
            redirectIfDone()
            return
        }

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

            val favorites = mutableListOf<String>()
            context.fetchAllPointsOfInterest().forEach {
                if (it.favorite) favorites.add(it.name)
                it.pictures.forEach { picturePath ->
                    File(picturePath).delete()
                }
            }

            context.contentResolver.delete(ZATON_PROVIDER_URI, null, null)
            apiPointsOfInterest.forEach {

                val favorite = favorites.contains(it.name)

                var localPicturesPaths: String = ""
                var pictureIndex: Int = 1

                it.picturesPaths.split("\n").forEach { picturePath ->

                    if (picturePath.isNotBlank()) {
                        localPicturesPaths += downloadImageAndStore(
                            context,
                            picturePath,
                            it.name + pictureIndex
                        )
                        localPicturesPaths += "\n"
                        pictureIndex++
                    }
                }

                val values = ContentValues().apply {
                    put(PointOfInterest::name.name, it.name)
                    put(PointOfInterest::description.name, it.description)
                    put(PointOfInterest::type.name, it.type)
                    put(PointOfInterest::lat.name, it.lat)
                    put(PointOfInterest::lon.name, it.lon)
                    put(PointOfInterest::pictures.name, localPicturesPaths)
                    put(PointOfInterest::favorite.name, favorite)
                }
                context.contentResolver.insert(ZATON_PROVIDER_URI, values)
                pictureIndex = 1
            }
            prefs
                .edit()
                .putBoolean(POI_DATA_EXISTS, true)
                .apply()

            redirectIfDone()
        }
    }


    fun fetchBusTimetable() {

        while (!finishedCheckingVersion) {

        }

        if (!newBusVersionAvailable) {
            prefs.edit()
                .putBoolean(BUS_DATA_EXISTS, true)
                .apply()
            redirectIfDone()
            return
        }

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
            prefs
                .edit()
                .putBoolean(BUS_DATA_EXISTS, true)
                .apply()

            redirectIfDone()
        }
    }


    private fun redirectIfDone() {
        if (prefs.getBoolean(POI_DATA_EXISTS, false) &&
            prefs.getBoolean(BUS_DATA_EXISTS, false)
        ) {
            context.sendBroadcast(Intent(context, ZatonReceiver::class.java))
        }
    }

}