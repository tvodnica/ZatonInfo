package hr.algebra.zatoninfo.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.ActivityMainBinding
import hr.algebra.zatoninfo.framework.getPreferences


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout //activity_main.xml
        val navView: NavigationView = binding.navView //navigation drawer
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration( //Spoji ove fragmente sa nav drawerom, tj njima prikaži hamburger
            setOf(
                R.id.nav_interests, R.id.nav_map, R.id.nav_settings, R.id.nav_activities,
                R.id.nav_busStopChooser, R.id.nav_favorites, R.id.nav_about, R.id.nav_welcome
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration) //Prikaži hamburger i ime fragmenta
        navView.setupWithNavController(navController)

        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}