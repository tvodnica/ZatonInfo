package hr.algebra.zatoninfo.api

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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

class ContactFetcher(private val context: Context) {

    private var contactApi: ContactApi
    private val prefs = context.getPreferences()


    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(CONTACT_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        contactApi = retrofit.create(ContactApi::class.java)
    }

    fun sendAMessage(message1: String) {

        if (message1.trim().isEmpty()) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    context.getString(R.string.cannot_send_empty_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }

        val apiContactItem = ApiContactItem(message1)

        val request = contactApi.sendMessage(apiContactItem)
        request.enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.body() != null) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            context.getString(R.string.messageSent),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            context.getString(R.string.messageNotSent),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "neće",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}