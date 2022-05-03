package hr.algebra.zatoninfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.zatoninfo.ui.MainActivity

class ZatonReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       context.startActivity(Intent(context, MainActivity::class.java))
    }
}