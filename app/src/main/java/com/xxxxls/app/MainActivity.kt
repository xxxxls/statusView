package com.xxxxls.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter =
            object : FragmentPagerAdapter(
                supportFragmentManager,
            ) {
                override fun getCount(): Int {
                    return 2
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return when (position) {
                        0 -> {
                            "XML"
                        }
                        else -> {
                            "CODE"
                        }
                    }
                }

                override fun getItem(position: Int): Fragment {
                    return when (position) {
                        0 -> {
                            StatusXmlFragment()
                        }
                        else -> {
                            StatusCodeFragment()
                        }
                    }
                }

            }
        findViewById<TabLayout>(R.id.tabLayout).setupWithViewPager(viewPager)
    }
}