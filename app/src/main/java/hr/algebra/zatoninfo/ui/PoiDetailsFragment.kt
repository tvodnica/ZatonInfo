package hr.algebra.zatoninfo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.databinding.FragmentPoiDetailsBinding

class PoiDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPoiDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPoiDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
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
        when(item.itemId){
            R.id.action_favorite -> {
                if(item.icon.constantState != getDrawable(requireContext(), R.drawable.ic_menu_favorite)?.constantState) {
                    item.setIcon(getDrawable(requireContext(), R.drawable.ic_menu_favorite))
                }
                else{
                    item.setIcon(R.drawable.ic_menu_not_favorite)
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }

}