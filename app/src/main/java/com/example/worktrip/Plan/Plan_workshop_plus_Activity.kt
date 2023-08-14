package com.example.worktrip.Plan

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityPlanWorkshopPlusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Plan_workshop_plus_Activity : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivityPlanWorkshopPlusBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlanWorkshopPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanPlusTitle
        toolbarTitle.text = "일정 추가"

        binding.btPlanPlusDone.setOnClickListener {
            var workshopdata = PlanWorkShopData() // 데이터 구조
            workshopdata.setuesrid(auth?.uid)
            workshopdata.settv_plan_budget(binding.etPlanBudget.text.toString())
            workshopdata.settv_plan_date(binding.etPlanDate.text.toString())
            workshopdata.settv_plan_people(binding.etPlanPeople.text.toString())
            workshopdata.settv_plan_title(binding.etPlanTitle.text.toString())
            //filter 설정 후 대입 필요

            db?.collection("user_workshop")
                ?.document()
                ?.set(workshopdata)

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