package com.example.worktrip.My

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityBookmarkBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


private lateinit var binding : ActivityBookmarkBinding

private lateinit var viewPager_bookmark: ViewPager2
private lateinit var tabLayout_bookmark: TabLayout

class BookmarkActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_bookmark)

        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_bookmark))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 (manifast에서 연결 필요)
        val toolbarTitle = view.findViewById<TextView>(R.id.tv_activity_bookmark_title)
        toolbarTitle.text="저장한 게시글"

        //탭레이아웃 설정
        viewPager_bookmark = findViewById<ViewPager2>(R.id.vp_activity_bookmark)
        tabLayout_bookmark = findViewById<TabLayout>(R.id.tl_activity_bookmark)

        val viewPagerAdapter = TabAdapter_bookmark(this)

        // fragment add
        viewPagerAdapter.addFragment(fragment_bookmark_list())
        viewPagerAdapter.addFragment(fragment_bookmark_commu())

        // adapter 연결
        viewPager_bookmark?.adapter = viewPagerAdapter
        viewPager_bookmark?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")
            }
        })
        // 뷰페이저와 탭레이아웃을 붙임
        TabLayoutMediator(tabLayout_bookmark, viewPager_bookmark){ tab, position ->
            when (position) {
                0 -> tab.text = "추천 정보"
                1 -> tab.text = "커뮤니티"
                else -> tab.text = "Tab ${position + 1}"
            }
        }.attach()
        //----------------------------------------------------
    }
    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search, menu)
        return true
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.it_toolbar_s_search -> {
                //검색 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                //intent = Intent(this, HomeSearchActivity::class.java)
                //startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }
}