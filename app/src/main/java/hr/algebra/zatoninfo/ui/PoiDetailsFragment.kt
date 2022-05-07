package hr.algebra.zatoninfo.ui

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.ContentValues
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
import hr.algebra.zatoninfo.ZATON_PROVIDER_URI
import hr.algebra.zatoninfo.databinding.FragmentPoiDetailsBinding
import hr.algebra.zatoninfo.framework.fetchAllPointsOfInterest
import hr.algebra.zatoninfo.model.PointOfInterest
import java.io.File


class PoiDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPoiDetailsBinding
    private lateinit var selectedPoi: PointOfInterest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPoiDetailsBinding.inflate(layoutInflater, container, false)
        loadData()
        bindData()
        return binding.root
    }

    private fun loadData() {
        val allPois = requireContext().fetchAllPointsOfInterest()
        val selectedPoiPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getLong(requireContext().getString(R.string.selectedInterest), 0)

        allPois.forEach {
            if (it._id == selectedPoiPref) {
                selectedPoi = it
            }
        }
    }

    private fun bindData() {
        selectedPoi.apply {
            binding.tvTitle.text = name
            binding.tvType.text = type
            binding.tvDescription.text = description

            binding.btnBook.isVisible = type == getString(R.string.trip)
            binding.mapHolder.isVisible = type != getString(R.string.trip)

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_specificPoi) as SupportMapFragment?
            mapFragment?.getMapAsync { map ->
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(lat, lon))
                )
                map.setMinZoomPreference(15f)
                map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))
                map.moveCamera(CameraUpdateFactory.zoomTo(15f))
            }

            pictures.forEach { picture ->

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


                binding.btnBook.setOnClickListener { btn ->
                    AlertDialog.Builder(requireContext()).apply {

                        setTitle("Send an enquiry")
                        setItems(
                            arrayOf(
                                "E-mail",
                                "Phone",
                                "WhatsApp"
                            )
                        ) { _, which -> sendEnquiry(which, selectedPoi) }
                        setNegativeButton(getString(R.string.cancel), null)
                        show()
                    }
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
                startActivity(this)
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
                        getString(R.string.whatsappNotInstalled),
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
        // Ovo ne radi i ne znam zašto, predugo tražim rješenje, odustajem
        menu.findItem(R.id.action_favorite).icon =
            if (selectedPoi.favorite) getDrawable(requireContext(), R.drawable.ic_menu_favorite)
            else getDrawable(requireContext(), R.drawable.ic_menu_not_favorite)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_favorite -> {
                selectedPoi.favorite = !selectedPoi.favorite
                if (selectedPoi.favorite) {
                    item.icon = getDrawable(requireContext(), R.drawable.ic_menu_favorite)
                } else {
                    item.icon = getDrawable(requireContext(), R.drawable.ic_menu_not_favorite)
                }
                val values = ContentValues().apply {
                    put(PointOfInterest::favorite.name, selectedPoi.favorite)
                }
                val uri = ContentUris.withAppendedId(ZATON_PROVIDER_URI, selectedPoi._id)
                requireContext().contentResolver.update(uri, values, null, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}