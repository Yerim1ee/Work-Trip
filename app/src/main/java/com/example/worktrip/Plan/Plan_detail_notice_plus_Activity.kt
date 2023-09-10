package com.example.worktrip.Plan

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanNoticeData
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.ActivityPlanDetailBudgetPlusBinding
import com.example.worktrip.databinding.ActivityPlanDetailNoticePlusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class Plan_detail_notice_plus_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailNoticePlusBinding

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var people_type:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailNoticePlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_timeline_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailNoticePlusTitle
        toolbarTitle.text = "공지 추가"


        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의


        binding.btPlanNoticePlusDone.setOnClickListener {


            if (binding.etPlanDetailNoticeTitle.text.toString().isEmpty()) {
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_LONG).show()
            }  else if (people_type.isEmpty()) {
                Toast.makeText(this, "공지할 멤버를 선택해주세요", Toast.LENGTH_LONG).show()
            }else if(binding.etPlanDetailNoticeContent.text.toString().isEmpty()){
                Toast.makeText(this, "내용을 설정해주세요", Toast.LENGTH_LONG).show()

            }
            else {
                val formatter_time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                // 현재시간으로 게시물 ID 생성
                val stringTime = formatter_time.format(System.currentTimeMillis())

                var datetimelineData = PlanNoticeData(
                    stringTime, // 타임라인의 아이디 만들어주기
                    binding.etPlanDetailNoticeTitle.text.toString(),
                    people_type,
                    binding.etPlanDetailNoticeContent.text.toString(),
                    "workshop_notice"

                ) // 데이터 구조

                // workshop 문서 생성
                db.collection("workshop")
                    .document(workshop_docID)
                    .collection("notice")
                    .document(stringTime)
                    .set(datetimelineData)

                finish()
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked: Boolean = view.isChecked

            when (view.getId()) {
                R.id.rb_plan_detail_notice_people_all -> {
                    if (checked) {
                        binding.rbPlanDetailNoticePeopleAll.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "전체"
                    }
                    else{
                        binding.rbPlanDetailNoticePeopleAll.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
                R.id.rb_plan_detail_notice_people_manager -> {
                    if (checked) {
                        binding.rbPlanDetailNoticePeopleManager.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "기획자"
                    }
                    else{
                        binding.rbPlanDetailNoticePeopleManager.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
                R.id.rb_plan_detail_notice_people_participant -> {
                    if (checked) {
                        binding.rbPlanDetailNoticePeopleParticipant.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "참가자"
                    }
                    else{
                        binding.rbPlanDetailNoticePeopleParticipant.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
                R.id.rb_plan_detail_notice_people_else-> {
                    if (checked) {
                        binding.rbPlanDetailNoticePeopleElse.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "기타"
                    }
                    else{
                        binding.rbPlanDetailNoticePeopleElse.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
            }
        }
    }
}