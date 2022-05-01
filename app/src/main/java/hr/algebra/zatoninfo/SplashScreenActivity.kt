package hr.algebra.zatoninfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import hr.algebra.zatoninfo.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.GlobalScope

class SplashScreenActivity : AppCompatActivity() {

    private val DELAY: Long = 5000
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

        startActivity(Intent(this, MainActivity::class.java))

       /* Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
            },
            DELAY
        )*/

    }

}