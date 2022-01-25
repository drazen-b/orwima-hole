package com.drazenbertic.hole_v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drazenbertic.hole_v2.adapters.ViewPagerAdapter
import com.drazenbertic.hole_v2.fragments.CoinsFragment
import com.drazenbertic.hole_v2.fragments.PortfolioFragment
import com.drazenbertic.hole_v2.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTabs()

    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(CoinsFragment(),"Coins")
        adapter.addFragment(PortfolioFragment(), "Portfolio")
        adapter.addFragment(SearchFragment(), "Search")
        vpStartMenu.adapter = adapter
        tlNavigation.setupWithViewPager(vpStartMenu)
    }
}
