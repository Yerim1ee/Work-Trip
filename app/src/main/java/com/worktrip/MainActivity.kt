package com.worktrip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.worktrip.Community.fragment_community
import com.worktrip.Home.fragment_home
import com.worktrip.My.MyFragment
import com.worktrip.Plan.PlanFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    private val homeFragment by lazy { fragment_home() }
    private val planFragment by lazy { PlanFragment() }
    private val communityFragment by lazy { fragment_community() }
    private val myFragment by lazy { MyFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // BottomNavigationView
        var bnv_activity_main = findViewById(R.id.bnv_activity_main) as BottomNavigationView

        // 바텀네비게이션 아이콘 클릭 이벤트
        bnv_activity_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.it_bottom_navigation_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                }
                R.id.it_bottom_navigation_plan -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, planFragment).commit()
                }
                R.id.it_bottom_navigation_community -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, communityFragment).commit()
                }

                R.id.it_bottom_navigation_my -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, myFragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.it_bottom_navigation_home
        }

    }


}