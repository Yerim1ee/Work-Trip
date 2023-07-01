package com.example.worktrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.worktrip.ui.theme.WorkTripTheme
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 하단 탭이 눌렸을 때 화면을 전환하기 위해선 이벤트 처리하기 위해 BottomNavigationView 객체 생성
        var bnv_activity_main_bnv = findViewById(R.id.bnv_activity_main_bnv) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_activity_main_bnv.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.it_bottom_navigation_home -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    val homeFragment = fragment_home()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                }
                R.id.it_bottom_navigation_plan -> {
                    val planFragment = fragment_home()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, planFragment).commit()
                }
                R.id.it_bottom_navigation_community -> {
                    val communityFragment = fragment_home()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, communityFragment).commit()
                }

                R.id.it_bottom_navigation_my -> {
                    val myFragment = fragment_home()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, myFragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.it_bottom_navigation_home
        }
    }

}



