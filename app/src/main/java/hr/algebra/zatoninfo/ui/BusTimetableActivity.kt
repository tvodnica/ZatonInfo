package hr.algebra.zatoninfo.ui

import android.app.AlertDialog
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.adapters.SectionsPagerAdapter
import hr.algebra.zatoninfo.databinding.ActivityBusTimetableBinding

class BusTimetableActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityBusTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBusTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        val busStop = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(getString(R.string.selectedBusStopName), getString(R.string.busTimetable))

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = busStop
        }

        binding.fab.setOnClickListener { view ->
            AlertDialog.Builder(this).apply {
                setTitle(context.getString(R.string.busInformation))
                setMessage(context.getString(R.string.allBusDestinationsInfo))
                setPositiveButton(context.getString(R.string.ok), null)
                show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}