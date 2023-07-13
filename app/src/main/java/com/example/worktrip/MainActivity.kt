package com.example.worktrip

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
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
import androidx.core.content.ContextCompat
import com.example.worktrip.ui.theme.WorkTripTheme
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    private val homeFragment by lazy { fragment_home() }
    private val planFragment by lazy { fragment_home() }
    private val communityFragment by lazy { fragment_home() }
    private val myFragment by lazy { fragment_home() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_main))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀

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

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_notice_search, menu)
        return true
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.it_toolbar_ns_notice -> {
                //검색 버튼 눌렀을 때
                Toast.makeText(applicationContext, "알림 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }

            R.id.it_toolbar_ns_search -> {
                //공유 버튼 눌렀을 때
                Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

}