package com.worktrip.Plan

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.worktrip.DataClass.PlanNoticeData
import com.worktrip.R
import com.worktrip.SocketApplication
import com.worktrip.databinding.ActivityPlanNoticeEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PlanNoticeEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanNoticeEditBinding

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var people_type:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanNoticeEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")


        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_notice_edit))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailNoticeEditTitle
        toolbarTitle.text = "공지 수정"


        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        // 넘겼던 데이터 가져오기
        val data = intent.getParcelableExtra<PlanNoticeData>("data")

        binding.etPlanDetailNoticeEditContent.setText(data?.plan_content)
        binding.etPlanDetailNoticeEditTitle.setText(data?.plan_title)
        when(data?.plan_people){
            "전체" ->{
                binding.rbPlanDetailNoticeEditPeopleAll.isChecked  = true
                people_type = "전체"
            }
            "기획자" ->{
                binding.rbPlanDetailNoticeEditPeopleManager.isChecked  = true
                people_type = "기획자"
            }
            "참가자" ->{
                binding.rbPlanDetailNoticeEditPeopleParticipant.isChecked  = true
                people_type = "참가자"
            }

            "기타" ->{
                binding.rbPlanDetailNoticeEditPeopleElse.isChecked  = true
                people_type = "기타"
            }

        }


        // 카테고리 받아오기
        binding.rgPlanDetailNoticeEditPeopleAll.setOnCheckedChangeListener{ group, checkId ->
            when(checkId){
                R.id.rb_plan_detail_notice_edit_people_all ->
                    people_type = "전체"
                R.id.rb_plan_detail_notice_edit_people_else ->
                    people_type = "기타"
                R.id.rb_plan_detail_notice_edit_people_manager ->
                    people_type = "기획자"
                R.id.rb_plan_detail_notice_edit_people_participant ->
                    people_type = "참가자"
            }
        }

        binding.btPlanNoticeEditPlusDone.setOnClickListener {

            if (binding.etPlanDetailNoticeEditTitle.text.toString().isEmpty()) {
                Toast.makeText(this, "공지 제목을 입력해주세요", Toast.LENGTH_LONG).show()
            }  else if (binding.etPlanDetailNoticeEditContent.text.toString().isEmpty()) {
                Toast.makeText(this, "공지 내용을 입력해주세요", Toast.LENGTH_LONG).show()
            }else if(people_type.isEmpty()){
                Toast.makeText(this, "공지할 멤버를 설정해주세요", Toast.LENGTH_LONG).show()
            }
            else {


                var datetimelineData = PlanNoticeData(
                    data?.docID, // 타임라인의 아이디 만들어주기
                    binding.etPlanDetailNoticeEditTitle.text.toString(),
                    people_type,
                    binding.etPlanDetailNoticeEditContent.text.toString(),
                    "workshop_notice"

                ) // 데이터 구조

                // workshop 문서 생성
                db.collection("workshop")
                    .document(workshop_docID)
                    .collection("notice")
                    .document(data?.docID.toString())
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
                        binding.rbPlanDetailNoticeEditPeopleAll.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "전체"
                    }
                    else{
                        binding.rbPlanDetailNoticeEditPeopleAll.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
                R.id.rb_plan_detail_notice_people_manager -> {
                    if (checked) {
                        binding.rbPlanDetailNoticeEditPeopleManager.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "기획자"
                    }
                    else{
                        binding.rbPlanDetailNoticeEditPeopleManager.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
                R.id.rb_plan_detail_notice_people_participant -> {
                    if (checked) {
                        binding.rbPlanDetailNoticeEditPeopleParticipant.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "참가자"
                    }
                    else{
                        binding.rbPlanDetailNoticeEditPeopleParticipant.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
                R.id.rb_plan_detail_notice_people_else-> {
                    if (checked) {
                        binding.rbPlanDetailNoticeEditPeopleElse.setTypeface(Typeface.DEFAULT_BOLD)
                        people_type = "기타"
                    }
                    else{
                        binding.rbPlanDetailNoticeEditPeopleElse.setTypeface(Typeface.DEFAULT)
                        people_type = ""
                    }
                }
            }
        }
    }
}