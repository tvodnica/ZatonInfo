package hr.algebra.zatoninfo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.testing.FakeReviewManager
import hr.algebra.zatoninfo.R

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

                    }
                } else {
                    // There was some problem, log or handle the error code.
                    //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }

        }
        return super.onPreferenceTreeClick(preference)

    }
}