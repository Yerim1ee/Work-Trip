package com.example.worktrip.Plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.worktrip.Plan.Adapter.Plan_tab_detail_Adapter
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.ActivityPlanWorkshopDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator

class Plan_workshop_details_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanWorkshopDetailsBinding
    private val tabTitleArray = arrayOf(
        "일정표",
        "예산",
        "인원",
        "공지"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlanWorkshopDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.vpPlanDetailsViewPager
        val tabLayout = binding.tlPlanDetailsTabLayout

        viewPager.adapter = Plan_tab_detail_Adapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_toolbar))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailTitle

        toolbarTitle.text = intent.getStringExtra("title")

        SocketApplication.prefs.setString("now_workshop_id",intent.getStringExtra("docID").toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_plusws_question, menu)
        return true
    }

}