package oliver.gaa_stats_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
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
        adapter.addFragment(ListViewFragment(), "Matches")
        adapter.addFragment(NewMatchFragment(), "New Match")
        adapter.addFragment(AccountFragment(), "Account")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.matches_tab)
        tabs.getTabAt(1)!!.setIcon(R.drawable.new_tab)
        tabs.getTabAt(2)!!.setIcon(R.drawable.account_tab)
    }
}