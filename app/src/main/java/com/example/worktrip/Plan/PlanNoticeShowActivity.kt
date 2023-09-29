package com.example.worktrip.Plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.worktrip.DataClass.PlanNoticeData
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityPlanNoticeShowBinding

class PlanNoticeShowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanNoticeShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_notice_show)

        binding = ActivityPlanNoticeShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 넘겼던 데이터 가져오기
        val data = intent.getParcelableExtra<PlanNoticeData>("data")


        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_notice_show))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailNoticeShowTitle
        toolbarTitle.text = data?.plan_title.toString()


        binding.etPlanDetailNoticeShowContent.setText(data?.plan_content)
        binding.etPlanDetailNoticeShowTitle.setText(data?.plan_title)
        when(data?.plan_people){
            "전체" ->{
                binding.rbPlanDetailNoticeShowPeopleAll.isChecked  = true
            }
            "기획자" ->{
                binding.rbPlanDetailNoticeShowPeopleManager.isChecked  = true
            }
            "참가자" ->{
                binding.rbPlanDetailNoticeShowPeopleParticipant.isChecked  = true
            }

            "기타" ->{
                binding.rbPlanDetailNoticeShowPeopleElse.isChecked  = true
            }

        }

        binding.btPlanNoticeShowPlusDone.setOnClickListener {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}