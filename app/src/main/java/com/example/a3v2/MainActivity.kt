package com.example.a3v2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.a3v2.adapters.MyFragmentPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var myFragmentPagerAdapter :   MyFragmentPagerAdapter     // fragment+titles list
    private lateinit var myViewPager            :   ViewPager                  // "eagerly" create+manage "pgs"
    private lateinit var tabLayout              :   TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        //----------------------
        handleFrags()
//        handleTabs()
    }

    private fun handleFrags() {
        myViewPager             =   findViewById(R.id.main_activity_fragment_container)
        myFragmentPagerAdapter  =   MyFragmentPagerAdapter(supportFragmentManager)
        myFragmentPagerAdapter.addFrag(HomeFragment(), "Home")
//        myFragmentPagerAdapter.addFrag(FragmentFav(drinkViewModel), "Favourite Drinks")
        myViewPager.adapter     =   myFragmentPagerAdapter
        myViewPager.currentItem =   0
    }
    //-----------------------------------------------
//    private fun handleTabs() {
//        tabLayout = findViewById(R.id.myTabLayout)
//        tabLayout.setupWithViewPager(myViewPager)                           // internal method
////        tabLayout.getTabAt(0)?.setIcon(R.drawable.tab_icon_all)       // adding new tabs
////        tabLayout.getTabAt(1)?.setIcon(R.drawable.tab_icon_fav)
//    }
}