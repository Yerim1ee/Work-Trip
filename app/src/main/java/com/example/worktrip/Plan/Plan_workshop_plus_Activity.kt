package com.example.worktrip.Plan

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.DataClass.PlanDetailDateData
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityPlanWorkshopEditBinding
import com.example.worktrip.databinding.ActivityPlanWorkshopPlusBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class Plan_workshop_plus_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanWorkshopPlusBinding
    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var startDate: String
    lateinit var endDate: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanWorkshopPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        // 오늘 날짜 받아오기
        // 게시글 now, past 분류하기 위해 받아옴
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val today = LocalDate.now()
        val todayDate_format = today.format(formatter) // 형식 지정
        val todayDate: LocalDate = LocalDate.parse(todayDate_format, formatter)

        // 게시글 ID 생성 위한 것
        val formatter_time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanPlusTitle
        toolbarTitle.text = "일정 추가"

        binding.ibPlanPlusDate.setOnClickListener {
            showDateRangePicker()
        }


        binding.btPlanPlusDone.setOnClickListener {
            if (binding.etPlanBudget.text.toString().isEmpty()) {
                Toast.makeText(this, "예산을 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.etPlanDate.text.toString().isEmpty()) {
                Toast.makeText(this, "날짜를 선택해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.etPlanPeople.text.toString().isEmpty()) {
                Toast.makeText(this, "참가 인원을 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.etPlanTitle.text.toString().isEmpty()) {
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.etPlanFilter.text.toString().isEmpty()) {
                Toast.makeText(this, "테마를 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                val now: LocalDate = LocalDate.parse(endDate, formatter)

                // 현재시간으로 게시물 ID 생성
                val stringTime = formatter_time.format(System.currentTimeMillis())

                var workshopdata = PlanWorkShopData(
                    stringTime,
                    !(now.isBefore(todayDate)),
                    startDate,
                    endDate,
                    binding.etPlanTitle.text.toString(),
                    binding.etPlanPeople.text.toString(),
                    binding.etPlanFilter.text.toString(),
                    binding.etPlanBudget.text.toString()
                ) // 데이터 구조

                val starDate_Local: LocalDate = LocalDate.parse(startDate, formatter)
                val endDate_Local: LocalDate = LocalDate.parse(endDate, formatter)
                var curDate_Local:LocalDate = starDate_Local


                // workshop 문서 생성
                db.collection("user_workshop")
                    .document("${auth.currentUser?.uid.toString()}")
                    .collection("workshop")
                    .document(stringTime)
                    .set(workshopdata)
                Log.d("aa",stringTime)

                // date 맞춰서 날짜별 문서 생성
                while(!(curDate_Local.isEqual(endDate_Local.plusDays(1)))){
                    var workshopdata = PlanDetailDateData(curDate_Local.format(formatter))
                    db.collection("user_workshop")
                        .document("${auth.currentUser?.uid.toString()}")
                        .collection("workshop")
                        .document(stringTime)
                        .collection("date")
                        .document(curDate_Local.format(formatter))
                        .set(workshopdata)
                    Log.d("aa",stringTime)

                    curDate_Local = curDate_Local.plusDays(1)
                    Log.d("aa",curDate_Local.format(formatter))

                }

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

    // 기간을 선택하기 위한 datePicker
    fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        // 타이틀 정하는 코드
        val title = "워크숍 기간 설정"
        builder.setTitleText(title)

        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnNegativeButtonClickListener { picker.dismiss() }
        picker.addOnPositiveButtonClickListener {
            startDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.first)
            endDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.second)
            binding.etPlanDate.setText("$startDate ~ $endDate")
        }
    }


}