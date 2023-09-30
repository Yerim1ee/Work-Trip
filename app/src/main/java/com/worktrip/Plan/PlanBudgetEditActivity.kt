package com.worktrip.Plan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.worktrip.DataClass.PlanBudgetData
import com.worktrip.R
import com.worktrip.SocketApplication
import com.worktrip.databinding.ActivityPlanBudgetEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.DecimalFormat


class PlanBudgetEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanBudgetEditBinding
    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var uri: Uri? = null
    lateinit var workshop_docID:String

    //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
    val t_dec_up = DecimalFormat("#,###")

    var radio_button_category  = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBudgetEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_budget_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailBudgetPlusTitle
        toolbarTitle.text = "사용내역 수정"


        // 넘겼던 데이터 가져오기
        val data = intent.getParcelableExtra<PlanBudgetData>("data")

        var budget = data?.plan_price.toString()
        var str_change_money_up = t_dec_up.parse(budget)
        binding.edPlanDetailBudgetPrice.setText(str_change_money_up.toString())

        imageShow(data?.docID.toString())
        binding.etPlanDetailBudgetContent.setText(data?.plan_content)
        binding.etPlanDetailBudgetPay.setText(data?.plan_pay)
        binding.etPlanDetailBudgetQuantity.setText(data?.plan_quantity)
        when(data?.plan_category){
            "코스" ->{
                binding.rbPlanBudgetCourse.isChecked  = true
                radio_button_category = "코스"
            }
            "맛집" ->{
                binding.rbPlanBudgetEat.isChecked  = true
                radio_button_category = "맛집"
            }
            "기타" ->{
                binding.rbPlanBudgetElse.isChecked  = true
                radio_button_category = "기타"
            }

            "숙소" ->{
                binding.rbPlanBudgetHouse.isChecked  = true
                radio_button_category = "숙소"
            }

            "프로그램"->{
                binding.rbPlanBudgetProgram.isChecked  = true
                radio_button_category = "프로그램"
            }

        }




        binding.ivPlanBudgetPlusRecipt.setOnClickListener {
            // ACTION_PICK을 사용하여 앨범을 호출한다.
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            registerForActivityResult.launch(intent)
        }

        // 카테고리 받아오기
        binding.rgPlanBudgetRadiogroup.setOnCheckedChangeListener{ group, checkId ->
            when(checkId){
                com.worktrip.R.id.rb_plan_budget_course ->
                    radio_button_category = "코스"
                com.worktrip.R.id.rb_plan_budget_eat ->
                    radio_button_category = "맛집"
                com.worktrip.R.id.rb_plan_budget_else ->
                    radio_button_category = "기타"
                com.worktrip.R.id.rb_plan_budget_house ->
                    radio_button_category = "숙소"
                com.worktrip.R.id.rb_plan_budget_program ->
                    radio_button_category = "프로그램"
            }
        }

        binding.btPlanBudgetPlusDone.setOnClickListener {
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

                if(!(uri.toString().equals(data?.plan_recipt.toString()))){
                    uri?.let { imageUpload(it, data?.docID.toString()) }
                }

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

    private fun imageUpload(uri: Uri, title:String) {
        // storage 인스턴스 생성
        val storage = Firebase.storage
        // storage 참조
        val storageRef = storage.getReference("plan_budget/${workshop_docID}")
        // 파일 경로와 이름으로 참조 변수 생성
        val mountainsRef = storageRef.child("${title}.png")


        val uploadTask = mountainsRef.putFile(uri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // 파일 업로드 성공
            Toast.makeText(this, "사진 업로드 성공", Toast.LENGTH_SHORT).show();
        }.addOnFailureListener {e ->

            // 파일 업로드 실패
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private fun imageShow( title:String) {

        val storage = FirebaseStorage.getInstance("gs://work-trip-c01ab.appspot.com/")
        val storageRef = storage.reference
        storageRef.child("plan_budget/${workshop_docID}/${title}.png").downloadUrl
            .addOnSuccessListener { uri -> //이미지 로드 성공시
                binding.ivPlanBudgetPlusImageIcon.visibility = View.GONE
            Glide.with(applicationContext)
                .load(uri)
                .into(binding.ivPlanBudgetPlusRecipt)
        }.addOnFailureListener { //이미지 로드 실패시
            Toast.makeText(applicationContext, "영수증 이미지 로드 실패", Toast.LENGTH_SHORT).show()
        }
    }

}