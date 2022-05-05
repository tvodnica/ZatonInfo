package hr.algebra.zatoninfo.ui

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentPoiDetailsBinding
import hr.algebra.zatoninfo.framework.fetchAllPointsOfInterest
import hr.algebra.zatoninfo.model.PointOfInterest
import java.io.File


class PoiDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPoiDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPoiDetailsBinding.inflate(layoutInflater, container, false)
        loadData()
        return binding.root
    }

    private fun loadData() {
        val allPois = requireContext().fetchAllPointsOfInterest()
        val selectedInterest = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getLong(requireContext().getString(R.string.selectedInterest), 0)
        allPois.forEach {
            if (it._id == selectedInterest) {
                binding.tvTitle.text = it.name
                binding.tvType.text = it.type
                binding.tvDescription.text = it.description

                binding.btnBook.isVisible = it.type == getString(R.string.trip)
                binding.mapHolder.isVisible = it.type != getString(R.string.trip)

                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map_specificPoi) as SupportMapFragment?
                mapFragment?.getMapAsync { map ->
                    map.addMarker(
                        MarkerOptions()
                            .position(LatLng(it.lat, it.lon))
                    )
                    map.setMinZoomPreference(15f)
                    map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lon)))
                    map.moveCamera(CameraUpdateFactory.zoomTo(15f))
                }

                it.pictures.forEach { picture ->

                    val image = ImageView(context).apply {
                        Picasso.get()
                            .load(File(picture))
                            .into(this)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                    binding.llImageHolder.addView(image)
                }
            }

            binding.btnBook.setOnClickListener { btn ->
                AlertDialog.Builder(requireContext()).apply {

                    setTitle("Send an enquiry")
                    setItems(
                        arrayOf(
                            "E-mail",
                            "Phone",
                            "WhatsApp"
                        )
                    ) { _, which -> sendEnquiry(which, it) }
                    setNegativeButton(getString(R.string.cancel), null)
                    show()
                }
            }
        }
    }

    private fun sendEnquiry(which: Int, it: PointOfInterest) {
        when (which) {
            //E-MAIL
            0 -> Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:" + getString(R.string.contactEmail))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.enquiryFor) + it.name)

                if (resolveActivity(requireContext().packageManager) != null) {
                    startActivity(this)
                }
            }
            //PHONE
            1 -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "00385913628422")))
            //WHATSAPP
            2 ->
                try {
                    Intent(Intent.ACTION_SENDTO).apply {
                        putExtra(Intent.EXTRA_TEXT, "Hi! I am interested about " + it.name)
                        type = "text/plain"
                        setPackage("com.whatsapp")
                        startActivity(this)
                    }
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(
                        requireContext(),
                        "WhatsApp is not installed on you device.",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                if (item.icon.constantState != getDrawable(
                        requireContext(),
                        R.drawable.ic_menu_favorite
                    )?.constantState
                ) {
                    item.setIcon(getDrawable(requireContext(), R.drawable.ic_menu_favorite))
                } else {
                    item.setIcon(R.drawable.ic_menu_not_favorite)
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }

}