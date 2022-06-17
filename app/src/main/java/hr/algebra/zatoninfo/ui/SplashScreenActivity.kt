package hr.algebra.zatoninfo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.ZatonReceiver
import hr.algebra.zatoninfo.ZatonService
import hr.algebra.zatoninfo.databinding.ActivitySplashScreenBinding
import hr.algebra.zatoninfo.framework.*
import okhttp3.internal.http.HttpMethod

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showUsername()
        showAnimations()
        redirect()
    }

    companion object{
        lateinit var binding: ActivitySplashScreenBinding
    }

    private fun showUsername() {
        val username = getPreferences().getString(getString(R.string.username_key), "")
        if (username != "") binding.tvHello.text = "Hi ${username}!"
    }

    private fun showAnimations() {

        if(getPoiDataVersion() != 0 && getBusDataVersion() != 0){
            binding.tvWelcomeTo.text = "Welcome back to"
            return
        }

        binding.tvWelcomeTo.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fadein)
        )

        binding.tvZaton.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fadeinlater)
        )

        binding.tvLoadingMessage.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fadeinlater)
        )
        binding.progressBar.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fadeinlater)
        )

    }

    private fun redirect() {
        if (!getPreferences().getBoolean(POI_DATA_EXISTS, false) ||
            !getPreferences().getBoolean(BUS_DATA_EXISTS, false)
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