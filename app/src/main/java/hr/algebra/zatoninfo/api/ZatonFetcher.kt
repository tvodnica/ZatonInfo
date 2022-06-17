package hr.algebra.zatoninfo.api

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import hr.algebra.zatoninfo.BUS_PROVIDER_URI
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.ZATON_PROVIDER_URI
import hr.algebra.zatoninfo.ZatonReceiver
import hr.algebra.zatoninfo.framework.*
import hr.algebra.zatoninfo.handler.downloadImageAndStore
import hr.algebra.zatoninfo.model.BusTimetableItem
import hr.algebra.zatoninfo.model.PointOfInterest
import hr.algebra.zatoninfo.ui.SplashScreenActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import kotlin.system.exitProcess

class ZatonFetcher(private val context: Context) {

    private var zatonApi: ZatonApi
    
    var newPoiVersionAvailable = true
    var newBusVersionAvailable = true

    var poiApiVersion = 0
    var busApiVersion = 0

    var noInternet = false

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        zatonApi = retrofit.create(ZatonApi::class.java)


        context.setPoiDataExists(false)
        context.setBusDataExists(false)

        /*
        SAMO ZA TESTIRANJE!!!!!!
        context.setPoiDataVersion(0)
         */

    }

    fun fetchAllData() {
        fetchApiVersions()
        Thread {
            while (!context.getPoiDataExists() || !context.getBusDataExists())
                if (!context.hasInternetAccess()) {
                    showNoInternetMessage()
                    break
                }
        }.start()
    }

    private fun fetchApiVersions() {
        showLoadingMessage("Checking for updates...")

        val request = zatonApi.fetchVersions()
        request.enqueue(object : Callback<List<ApiVersions>> {
            override fun onResponse(
                call: Call<List<ApiVersions>>,
                response: Response<List<ApiVersions>>
            ) {
                response.body()?.let { list ->

                    list.forEach {

                        if (it.name == context.getString(R.string.pointsOfInterest)) {
                            if (it.version == context.getPoiDataVersion()) {
                                newPoiVersionAvailable = false
                            }
                            poiApiVersion = it.version
                        }

                        if (it.name == context.getString(R.string.busTimetable)) {
                            if (it.version == context.getBusDataVersion()) {
                                newBusVersionAvailable = false
                            }
                        }
                        busApiVersion = it.version
                    }
                }
                fetchPois()
            }

            override fun onFailure(call: Call<List<ApiVersions>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                showErrorMessage()
            }
        })
    }

    //------------------   POIS   --------------------------

    private fun fetchPois() {
        showLoadingMessage("Downloading interests")

        if (!newPoiVersionAvailable) {
            context.setPoiDataExists(true)
            redirectIfDone()
            fetchBusTimetable()
            return
        }

        val request = zatonApi.fetchItems()
        request.enqueue(object : Callback<List<ApiPointOfInterest>> {
            override fun onResponse(
                call: Call<List<ApiPointOfInterest>>,
                response: Response<List<ApiPointOfInterest>>
            ) {
                response.body()?.let {
                    populatePoiItems(it)
                }
            }

            override fun onFailure(call: Call<List<ApiPointOfInterest>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                showErrorMessage()
            }
        })
    }

    private fun populatePoiItems(apiPointsOfInterest: List<ApiPointOfInterest>) {
        GlobalScope.launch {

            val favorites = mutableListOf<String>()
            context.fetchAllPointsOfInterest().forEach {
                if (it.favorite) favorites.add(it.name)
                it.pictures.forEach { picturePath ->
                    File(picturePath).delete()
                }
            }
            var currentImage = 0
            var allImages = 0
            apiPointsOfInterest.forEach {
                it.picturesPaths.split("\n").forEach { s ->
                    if (s.isNotBlank()) allImages += 1
                }
            }

            context.contentResolver.delete(ZATON_PROVIDER_URI, null, null)
            apiPointsOfInterest.forEach {

                val favorite = favorites.contains(it.name)

                var localPicturesPaths = ""
                var pictureIndex = 1

                it.picturesPaths.split("\n").forEach { picturePath ->

                    if (picturePath.isNotBlank()) {
                        localPicturesPaths += downloadImageAndStore(
                            context,
                            picturePath,
                            it.name + pictureIndex
                        )
                        localPicturesPaths += "\n"
                        pictureIndex++
                        currentImage++
                        showLoadingMessage("Downloading images (${currentImage}/${allImages})")
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
            context.setPoiDataExists(true)

            redirectIfDone()
            fetchBusTimetable()
        }
    }

    //------------------   BUS   --------------------------

    private fun fetchBusTimetable() {
        showLoadingMessage("Downloading bus timetable")

        if (!newBusVersionAvailable) {
            context.setBusDataExists(true)
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
                showErrorMessage()
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
            showLoadingMessage("Finishing up")
            context.setBusDataExists(true)
            redirectIfDone()
        }
    }


    //------------------   OTHER   --------------------------

    private fun redirectIfDone() {
        if(noInternet){
            return
        }
        if (context.getPoiDataExists() && context.getBusDataExists()) {
            showLoadingMessage("Finishing up")
            context.setPoiDataVersion(poiApiVersion)
            context.setBusDataVersion(busApiVersion)
            context.sendBroadcast(Intent(context, ZatonReceiver::class.java))

        }
    }

    private fun showLoadingMessage(message: String) {
        Handler(Looper.getMainLooper()).post {
            SplashScreenActivity.binding.tvLoadingMessage.setText(message)
        }
    }

    private fun showErrorMessage() {
        if (noInternet){
            return
        }
        Handler(Looper.getMainLooper()).post {
            AlertDialog.Builder(SplashScreenActivity.binding.loadingSection.context).apply {
                setTitle(R.string.error)
                setMessage(R.string.downloadErrorMessage)
                setCancelable(false)
                setPositiveButton(R.string.exit) { _, _ -> exitProcess(0) }
                show()
            }
        }
    }

    private fun showNoInternetMessage() {
        noInternet = true
        Handler(Looper.getMainLooper()).post {
            AlertDialog.Builder(SplashScreenActivity.binding.loadingSection.context).apply {
                setTitle(R.string.no_internet)
                setMessage(R.string.internetLost)
                setCancelable(false)
                setPositiveButton(R.string.exit) { _, _ -> exitProcess(0) }
                show()
            }
        }
    }
}