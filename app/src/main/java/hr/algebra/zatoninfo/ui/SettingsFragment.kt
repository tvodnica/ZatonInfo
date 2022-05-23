package hr.algebra.zatoninfo.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.framework.getPreferences


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference == preferenceScreen.findPreference(getString(R.string.rate_us))) {

            if (requireContext().getPreferences().getBoolean(RATE_US_ALREADY_CLICKED, false)) {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + requireContext().packageName)
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().packageName)
                        )
                    )
                }
            } else {

                val manager =
                    ReviewManagerFactory.create(requireContext())
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
                        requireContext().getPreferences().edit().putBoolean(RATE_US_ALREADY_CLICKED, true).apply()
                    } else {
                        // There was some problem, log or handle the error code.
                        //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}