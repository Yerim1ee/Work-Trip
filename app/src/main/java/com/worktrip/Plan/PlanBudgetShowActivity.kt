package com.worktrip.Plan

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.worktrip.DataClass.PlanBudgetData
import com.worktrip.R
import com.worktrip.SocketApplication
import com.worktrip.databinding.ActivityPlanBudgetShowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.DecimalFormat

class PlanBudgetShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanBudgetShowBinding
    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    lateinit var workshop_docID:String

    //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
    val t_dec_up = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBudgetShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

        // 넘겼던 데이터 가져오기
        val data = intent.getParcelableExtra<PlanBudgetData>("data")

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_budget_show))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailBudgetShowTitle
        toolbarTitle.text = "사용내역 보기"

        var budget = data?.plan_price.toString()
        var str_change_money_up = t_dec_up.parse(budget)
        binding.edPlanDetailBudgetShowPrice.setText(str_change_money_up.toString())

        imageShow(data?.docID.toString())
        binding.etPlanDetailBudgetShowContent.setText(data?.plan_content)
        binding.etPlanDetailBudgetShowPay.setText(data?.plan_pay)
        binding.etPlanDetailBudgetShowQuantity.setText(data?.plan_quantity)
        when(data?.plan_category){
            "코스" ->{
                binding.rbPlanBudgetShowCourse.isChecked  = true
            }
            "맛집" ->{
                binding.rbPlanBudgetShowEat.isChecked  = true
            }
            "기타" ->{
                binding.rbPlanBudgetShowElse.isChecked  = true
            }

            "숙소" ->{
                binding.rbPlanBudgetShowHouse.isChecked  = true
            }

            "프로그램"->{
                binding.rbPlanBudgetShowProgram.isChecked  = true
            }

        }

        binding.btPlanBudgetShowDone.setOnClickListener {
            finish()
        }





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun imageShow( title:String) {
        val storage = FirebaseStorage.getInstance("gs://work-trip-c01ab.appspot.com/")
        val storageRef = storage.reference
        storageRef.child("plan_budget/${workshop_docID}/${title}.png").downloadUrl
            .addOnSuccessListener { uri -> //이미지 로드 성공시
                binding.ivPlanBudgetShowImageIcon.visibility = View.GONE
                Glide.with(applicationContext)
                    .load(uri)
                    .into(binding.ivPlanBudgetShowRecipt)
            }.addOnFailureListener { //이미지 로드 실패시
                Toast.makeText(applicationContext, "영수증 이미지 로드 실패", Toast.LENGTH_SHORT).show()
            }
    }

}