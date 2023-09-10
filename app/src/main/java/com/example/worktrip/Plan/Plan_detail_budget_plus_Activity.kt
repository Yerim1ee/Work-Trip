package com.example.worktrip.Plan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.ActivityPlanDetailBudgetPlusBinding
import com.example.worktrip.databinding.ActivityPlanDetailTimelinePlusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class Plan_detail_budget_plus_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailBudgetPlusBinding

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var uri: Uri? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBudgetPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의


        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_budget_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailBudgetPlusTitle
        toolbarTitle.text = "사용내역 추가"




        binding.ivPlanBudgetPlusRecipt.setOnClickListener {
            // ACTION_PICK을 사용하여 앨범을 호출한다.
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            registerForActivityResult.launch(intent)
        }

        // 카테고리 받아오기
        var radio_button_category  = ""
        binding.rgPlanBudgetRadiogroup.setOnCheckedChangeListener{ group, checkId ->
            when(checkId){
                R.id.rb_plan_budget_course ->
                    radio_button_category = "코스"
                R.id.rb_plan_budget_eat ->
                    radio_button_category = "맛집"
                R.id.rb_plan_budget_else ->
                    radio_button_category = "기타"
                R.id.rb_plan_budget_house ->
                    radio_button_category = "숙소"
                R.id.rb_plan_budget_program ->
                    radio_button_category = "프로그램"
            }
        }

        binding.btPlanBudgetPlusDone.setOnClickListener {
            //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
            val t_dec_up = DecimalFormat("#,###")

            uri?.let { imageUpload(it) }

            if (binding.edPlanDetailBudgetPrice.text.toString().isEmpty()) {
                Toast.makeText(this, "사용 가격을 입력해주세요", Toast.LENGTH_LONG).show()
            }  else if (binding.etPlanDetailBudgetQuantity.text.toString().isEmpty()) {
                Toast.makeText(this, "수량을 입력해주세요", Toast.LENGTH_LONG).show()
            }else if(binding.etPlanDetailBudgetPay.text.toString().isEmpty()){
                Toast.makeText(this, "결제 수단을 설정해주세요", Toast.LENGTH_LONG).show()

            } else if (binding.etPlanDetailBudgetContent.text.toString().isEmpty()) {
                Toast.makeText(this, "세부 내용을 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (radio_button_category.isEmpty()) {
                Toast.makeText(this, "카테고리를 선택해주세요", Toast.LENGTH_LONG).show()
            }
            else {

                var budget = binding.edPlanDetailBudgetPrice.text.toString().toInt()
                var str_change_money_up = t_dec_up.format(budget)
                binding.edPlanDetailBudgetPrice.setText(str_change_money_up)

                val formatter_time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                // 현재시간으로 게시물 ID 생성
                val stringTime = formatter_time.format(System.currentTimeMillis())

                var datetimelineData = PlanBudgetData(
                    stringTime, // 타임라인의 아이디 만들어주기
                    binding.edPlanDetailBudgetPrice.text.toString(),
                    radio_button_category,
                    binding.etPlanDetailBudgetQuantity.text.toString(),
                    binding.etPlanDetailBudgetPay.text.toString(),
                    binding.etPlanDetailBudgetContent.text.toString(),
                    uri.toString()

                    ) // 데이터 구조

                // workshop 문서 생성
                db.collection("workshop")
                    .document(workshop_docID)
                    .collection("budget")
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

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    // 변수 uri에 전달 받은 이미지 uri를 넣어준다.
                    uri = result.data?.data!!
                    // 이미지를 ImageView에 표시한다.
                    binding.ivPlanBudgetPlusImageIcon.visibility = View.GONE
                    binding.ivPlanBudgetPlusRecipt.setImageURI(uri)
                }
            }
        }

    private fun imageUpload(uri: Uri) {
        // storage 인스턴스 생성
        val storage = Firebase.storage
        // storage 참조
        val storageRef = storage.getReference("plan_budget/${auth.uid}")
        // 파일 경로와 이름으로 참조 변수 생성
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mountainsRef = storageRef.child("${fileName}.png")


        val uploadTask = mountainsRef.putFile(uri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // 파일 업로드 성공
            Toast.makeText(this, "사진 업로드 성공", Toast.LENGTH_SHORT).show();
        }.addOnFailureListener {e ->

            // 파일 업로드 실패
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
        }
    }


}