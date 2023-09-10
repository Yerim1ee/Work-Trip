package com.example.worktrip.Plan

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityPlanWorkshopEditBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class Plan_workshop_edit_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanWorkshopEditBinding
    lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var startDate:String
    lateinit var endDate:String

    //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
    val t_dec_up = DecimalFormat("#,###")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlanWorkshopEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        // 오늘 날짜 받아오기
        // 게시글 now, past 분류하기 위해 받아옴
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val today = LocalDate.now()
        val todayDate_format = today.format(formatter) // 형식 지정
        val todayDate: LocalDate = LocalDate.parse(todayDate_format, formatter)

        // 넘겼던 데이터 가져오기
        val data = intent.getParcelableExtra<PlanWorkShopData>("data")

        // date 초기화
        startDate = data!!.tv_plan_date_start.toString()
        endDate = data!!.tv_plan_date_end.toString()

        // 데이터 기반 화면에 보여주기
        binding.etPlanDate.setText(data.tv_plan_date_start.toString() + " ~ " + data.tv_plan_date_end.toString() )
        binding.etPlanPeople.setText(data.tv_plan_people.toString())
        binding.etPlanTitle.setText(data.tv_plan_title.toString())
        binding.etPlanFilter.setText(data.tv_plan_filter.toString())

        var budget = data.tv_plan_budget.toString()
        var str_change_money_up = t_dec_up.parse(budget)
        binding.etPlanBudget.setText(str_change_money_up.toString())

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanPlusTitle
        toolbarTitle.text = "설정 변경"

        binding.ibPlanPlusDate.setOnClickListener {
            showDateRangePicker()
        }


        binding.btPlanPlusDone.setOnClickListener {


            if( binding.etPlanBudget.text.toString().isEmpty()){
                Toast.makeText(this, "예산을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else if( binding.etPlanDate.text.toString().isEmpty()){
                Toast.makeText(this, "날짜를 선택해주세요", Toast.LENGTH_LONG).show()

            }
            else if( binding.etPlanPeople.text.toString().isEmpty() ){
                Toast.makeText(this, "참가 인원을 입력해주세요", Toast.LENGTH_LONG).show()

            }
            else if( binding.etPlanTitle.text.toString().isEmpty()){
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_LONG).show()

            }
            else if( binding.etPlanFilter.text.toString().isEmpty()){
                Toast.makeText(this, "테마를 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else{
                var budget = binding.etPlanBudget.text.toString().toInt()
                var str_change_money_up = t_dec_up.format(budget)
                binding.etPlanBudget.setText(str_change_money_up)

                val now: LocalDate = LocalDate.parse(endDate, formatter)

                var workshopdata = PlanWorkShopData(
                    data.docID,
                    !(now.isBefore(todayDate)),
                    startDate,
                    endDate,
                    binding.etPlanTitle.text.toString(),
                    binding.etPlanPeople.text.toString(),
                    binding.etPlanFilter.text.toString(),
                    binding.etPlanBudget.text.toString()

                ) // 데이터 구조

                // workshop 문서 생성
                db.collection("workshop")
                    .document(data.docID.toString())
                    .set(workshopdata)

                var workshopuser_data = PlanWorkShopUserData(
                    data.docID,
                    startDate,
                    "기획자"

                )
                // 자신의 id에 추가하기
                db.collection("user_workshop")
                    .document(auth.uid.toString())
                    .collection("workshop_list")
                    .document(data.docID.toString())
                    .set(workshopuser_data)

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
    fun showDateRangePicker(){
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        // 타이틀 정하는 코드
        val title = "워크숍 기간 설정"
        builder.setTitleText(title)

        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnNegativeButtonClickListener{ picker.dismiss() }
        picker.addOnPositiveButtonClickListener {
            startDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.first)
            endDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.second)
            binding.etPlanDate.setText("$startDate ~ $endDate")
        }
    }


}