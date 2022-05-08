package hr.algebra.zatoninfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.btnGetStarted.setOnClickListener {
            val navDrawer: DrawerLayout =
                requireActivity().findViewById(R.id.drawer_layout)
            navDrawer.openDrawer(GravityCompat.START)
        }

    }
}
