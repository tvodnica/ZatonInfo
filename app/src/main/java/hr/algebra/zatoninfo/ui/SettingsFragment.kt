package hr.algebra.zatoninfo.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import hr.algebra.zatoninfo.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
       addPreferencesFromResource(R.xml.settings)
    }

}