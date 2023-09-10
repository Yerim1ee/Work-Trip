package com.example.worktrip.Plan

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.ActivityPlanBudgetEditBinding
import com.example.worktrip.databinding.ActivityPlanDetailBudgetPlusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

class PlanBudgetEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanBudgetEditBinding
    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var uri: Uri? = null

    //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
    val t_dec_up = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBudgetEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_budget_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailBudgetPlusTitle
        toolbarTitle.text = "사용내역 수정"

        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

        // 넘겼던 데이터 가져오기
        val data = intent.getParcelableExtra<PlanBudgetData>("data")

        var budget = data?.plan_price.toString()
        var str_change_money_up = t_dec_up.parse(budget)
        binding.edPlanDetailBudgetPrice.setText(str_change_money_up.toString())

        binding.etPlanDetailBudgetContent.setText(data?.plan_content)
        binding.etPlanDetailBudgetPay.setText(data?.plan_pay)
        binding.etPlanDetailBudgetQuantity.setText(data?.plan_quantity)
        Log.d("aaaa", data?.plan_category.toString())
        when(data?.plan_category){
            "코스" ->
                binding.rbPlanBudgetCourse.isChecked  = true

                "맛집" ->
                        binding.rbPlanBudgetEat.isChecked  = true
                    "기타" ->
                        binding.rbPlanBudgetElse.isChecked  = true
                    "숙소" ->
                        binding.rbPlanBudgetHouse.isChecked  = true
                    "프로그램"->
            binding.rbPlanBudgetProgram.isChecked  = true
        }

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의




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


                var datetimelineData = PlanBudgetData(
                    data?.docID, // 타임라인의 아이디 만들어주기
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