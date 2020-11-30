package oliver.gaa_stats_tracker.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import oliver.gaa_stats_tracker.fragments.AccountFragment
import oliver.gaa_stats_tracker.fragments.ListViewFragment
import oliver.gaa_stats_tracker.fragments.NewMatchFragment

internal class ViewPagerAdapter(fm: FragmentManager?):
    FragmentPagerAdapter(fm!!){

    override fun getItem(position: Int): Fragment {

        return when(position){

            0 ->{
                ListViewFragment()
            }

            1 -> {
                NewMatchFragment()
            }

            2 -> {
                AccountFragment()
            }
            else -> ListViewFragment()
        }
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

}