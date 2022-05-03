package hr.algebra.zatoninfo

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.JobIntentService
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.api.ZatonFetcher
import hr.algebra.zatoninfo.ui.DATA_EXISTS
import java.util.logging.Handler

class ZatonService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {

        ZatonFetcher(this).fetchItems()
        ZatonFetcher(this).fetchBusTimetable()
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putBoolean(DATA_EXISTS, true)
            .apply()
        SystemClock.sleep(500)
        sendBroadcast(Intent(this, ZatonReceiver::class.java))
    }


    companion object {
        fun enqueue(context: Context, intent: Intent) {
            enqueueWork(context, ZatonService::class.java, 1, intent)
        }
    }
}