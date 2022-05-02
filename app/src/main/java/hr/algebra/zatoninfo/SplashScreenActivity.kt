package hr.algebra.zatoninfo

import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.GlobalScope

class SplashScreenActivity : AppCompatActivity() {

    private val DELAY: Long = 5000
    private val DATA_EXISTS: String = " hr.algebra.zatoninfo.data_exists"
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showAnimations()
        redirect()
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
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DATA_EXISTS, false)) {
            if (!hasInternetAccess()) {
                AlertDialog.Builder(this).apply {
                    setTitle("No internet")
                    setMessage("Internet is required for first time use. Please enable internet on your device to download information.")
                    setCancelable(false)
                    setPositiveButton("OK") { _, _ -> redirect() }
                    show()
                }
                return
            }
            //SKINI PODATKE

        } else {
            if (!hasInternetAccess()) {
                AlertDialog.Builder(this).apply {
                    setTitle("No internet")
                    setMessage("Could not check for updates because there was no internet. Please enable internet next time to make sure you have the newest information.")
                    setCancelable(false)
                    setPositiveButton("OK", null)
                    show()
                }
            }
            //PROVJERI VERZIJU I SKINI NOVE PODATKE AKO TREBA
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
            },
            DELAY
        )
    }


    private fun hasInternetAccess(): Boolean {

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}