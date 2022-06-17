package hr.algebra.zatoninfo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.zatoninfo.api.ZatonFetcher

class ZatonService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        ZatonFetcher(this).fetchAllData()
    }

    companion object {
        fun enqueue(context: Context, intent: Intent) {
            enqueueWork(context, ZatonService::class.java, 1, intent)
        }
    }
}