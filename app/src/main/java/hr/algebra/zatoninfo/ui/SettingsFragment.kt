package hr.algebra.zatoninfo.ui

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.api.ContactFetcher
import hr.algebra.zatoninfo.framework.RATE_US_ALREADY_CLICKED
import hr.algebra.zatoninfo.framework.getPreferences
import hr.algebra.zatoninfo.framework.hasInternetAccess


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
                        requireContext().getPreferences().edit()
                            .putBoolean(RATE_US_ALREADY_CLICKED, true).apply()
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
        if (preference == preferenceScreen.findPreference(getString(R.string.leave_a_comment_key))) {

            if (!requireContext().hasInternetAccess()) {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.no_internet_message),
                    Toast.LENGTH_LONG
                ).show()
            } else {

                val et = EditText(requireContext()).apply {
                    setPadding(50)
                    setLines(4)
                    gravity = Gravity.TOP
                    hint = context.getString(R.string.your_message_here)
                }

                AlertDialog.Builder(requireContext()).apply {
                    setTitle(getString(R.string.leave_a_comment_key))
                    setNegativeButton(getString(R.string.cancel), null)
                    setPositiveButton(getString(R.string.ok)) { _, _ ->
                        ContactFetcher(requireContext()).sendAMessage(et.text.toString())
                    }
                    setView(et)
                    show()
                }
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}