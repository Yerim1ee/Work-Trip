package com.worktrip.Plan

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.worktrip.DataClass.PlanDetailDateData
import com.worktrip.DataClass.PlanWorkShopData
import com.worktrip.DataClass.PlanWorkShopUserData
import com.worktrip.DataClass.UserBaseData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.worktrip.R
import com.worktrip.databinding.ActivityPlanWorkshopPlusBinding
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class Plan_workshop_plus_Activity : AppCompatActivity(){
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
            //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
            val t_dec_up = DecimalFormat("#,###")


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

                var budget = binding.etPlanBudget.text.toString()
                var str_change_money_up = t_dec_up.format(budget.toInt())
                binding.etPlanBudget.setText(str_change_money_up)

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
                db.collection("workshop")
                    .document()
                    .set(workshopdata)



                // 날짜 추가 & document id 저장
                db.collection("workshop")
                    .get()
                    .addOnSuccessListener {
                        result->
                        for (document in result) {
                            val item = document.toObject(PlanWorkShopData::class.java)

                            // date 맞춰서 날짜별 문서 생성
                            if(item.docID.equals(stringTime)){
                                while(!(curDate_Local.isEqual(endDate_Local.plusDays(1)))){

                                    var workshopdata = PlanDetailDateData(curDate_Local.format(formatter))
                                    db.collection("workshop")
                                        .document(document.id)
                                        .collection("date")
                                        .document(curDate_Local.format(formatter))
                                        .set(workshopdata)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "워크숍이 추가되었습니다.",Toast.LENGTH_LONG).show()
                                        }

                                    curDate_Local = curDate_Local.plusDays(1)

                                }



                                db.collection("workshop")
                                    .document(document.id)
                                    .update("docID", document.id)

                                db.collection("user")
                                    .document(auth.uid.toString())
                                    .get()
                                    .addOnSuccessListener { result -> // 성공
                                        val item = result.toObject(UserBaseData::class.java)
                                        if (item != null) {
                                            var workshopuser_data = PlanWorkShopUserData(
                                                document.id,
                                                startDate,
                                                "기획자",
                                                auth.uid.toString(),
                                                item.userName.toString()
                                                )


                                            // 자신의 id에 추가하기
                                            db.collection("user_workshop")
                                                .document(auth.uid.toString())
                                                .collection("workshop_list")
                                                .document(document.id)
                                                .set(workshopuser_data)
                                        }
                                    }
                                    .addOnFailureListener {
                                    }



                            }

                        }

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