package hr.algebra.zatoninfo.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.testing.FakeReviewManager
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.framework.preferences

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference == preferenceScreen.findPreference(getString(R.string.rate_us))) {

            val manager =
                FakeReviewManager(requireContext()) //ReviewManagerFactory.create(requireContext())
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        Toast.makeText(requireContext(), "Sve 5!", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    // There was some problem, log or handle the error code.
                    //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                    Toast.makeText(requireContext(), "Greška", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return super.onPreferenceTreeClick(preference)

    }
}