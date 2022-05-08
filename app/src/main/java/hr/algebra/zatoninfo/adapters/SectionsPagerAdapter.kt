package hr.algebra.zatoninfo.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.algebra.zatoninfo.R
import hr.algebra.zatoninfo.ui.BusTimetableFragment

private val TAB_TITLES = arrayOf(
    R.string.thisBusStop,
    R.string.startingPoint
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var startingPoint = false
        when(position){
            0 -> startingPoint = false
            1 -> startingPoint = true
        }
        return BusTimetableFragment(startingPoint)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}