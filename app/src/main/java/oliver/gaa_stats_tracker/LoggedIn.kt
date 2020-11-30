package oliver.gaa_stats_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.logged_in.*
import kotlinx.android.synthetic.main.login_page.*
import oliver.gaa_stats_tracker.fragments.AccountFragment
import oliver.gaa_stats_tracker.fragments.ListViewFragment
import oliver.gaa_stats_tracker.fragments.NewMatchFragment
import oliver.gaa_stats_tracker.fragments.adapters.ViewPagerAdapter
import org.jetbrains.anko.AnkoLogger

class LoggedIn : AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logged_in)
        setUpTabs()
        loggedInLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ListViewFragment(),"Home")
        adapter.addFragment(NewMatchFragment(), "New Match")
        adapter.addFragment(AccountFragment(), "Account")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.home_image)
        tabs.getTabAt(1)!!.setIcon(R.drawable.plus_image)
        tabs.getTabAt(2)!!.setIcon(R.drawable.account_image)
    }
}