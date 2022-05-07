package hr.algebra.zatoninfo.ui

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.ZatonReceiver
import hr.algebra.zatoninfo.ZatonService
import hr.algebra.zatoninfo.databinding.ActivitySplashScreenBinding
import hr.algebra.zatoninfo.framework.Preferences
import hr.algebra.zatoninfo.framework.hasInternetAccess

const val POI_DATA_EXISTS = " hr.algebra.zatoninfo.poi_data_exists"
const val BUS_DATA_EXISTS = " hr.algebra.zatoninfo.bus_data_exists"


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showUsername()
        showAnimations()
        redirect()
    }

    private fun showUsername() {
        val username = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(getString(R.string.username_key), "")
        if (username != "") binding.tvHello.text = "Hi ${username}!"
    }

    private fun showAnimations() {
        binding.tvHello.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.rotate)
        )

        binding.tvWelcomeTo.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.slide)
        )

        binding.tvZaton.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fadein)
        )

        binding.loadingSection.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fadeinlater)
        )
    }

    private fun redirect() {
        if (!Preferences().getBoolean(POI_DATA_EXISTS, false) ||
            !Preferences().getBoolean(BUS_DATA_EXISTS, false)
        ) {
            if (!hasInternetAccess()) {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.no_internet)
                    setMessage(R.string.internet_message_first_time)
                    setCancelable(false)
                    setPositiveButton(R.string.ok) { _, _ -> redirect() }
                    show()
                }
                return
            }
        } else {
            if (!hasInternetAccess()) {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.no_internet)
                    setMessage(R.string.internet_message_update)
                    setCancelable(false)
                    setPositiveButton(R.string.ok) { _, _ ->
                        sendBroadcast(
                            Intent(
                                this@SplashScreenActivity,
                                ZatonReceiver::class.java
                            )
                        )
                    }
                    show()
                }
                return
            }
        }
        Intent(this, ZatonService::class.java).apply {
            ZatonService.enqueue(this@SplashScreenActivity, this)
        }
    }

}