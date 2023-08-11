package com.example.worktrip

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.Home.HomeSearchActivity
import com.example.worktrip.Home.fragment_home
import com.example.worktrip.My.MyFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    private val homeFragment by lazy { fragment_home() }
    private val planFragment by lazy { fragment_home() }
    private val communityFragment by lazy { fragment_home() }
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