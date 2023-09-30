package com.worktrip.My

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import com.worktrip.DataClass.UserBaseData
import com.worktrip.R
import com.worktrip.SocketApplication
import com.worktrip.databinding.ActivityMyChangeInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class My_change_info_Activity : AppCompatActivity() {


    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var userID : String
    lateinit var userName: String
    lateinit var date: String
    lateinit var company: String
    lateinit var food : String
    lateinit var travel: String
    lateinit var sleep: String
    lateinit var reports: String
    lateinit var course: String

    // 뷰 바인딩을 위한 객체 획득
    private lateinit var binding: ActivityMyChangeInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMyChangeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_my_change_info))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvMyChangeInfoTitle
        toolbarTitle.text = "정보 수정"


        // 초기 정보 설정
        db.collection("user")
            .document(auth.uid.toString())
            .get()
            .addOnSuccessListener {result ->
                val item = result.toObject(UserBaseData::class.java)
                if (item != null) {

                    userID = item.userID.toString()
                    userName = item.userName.toString()
                    date = item.date.toString()
                    company = item.company.toString()
                    food = item.food.toString()
                    travel = item.travel.toString()
                    sleep = item.sleep.toString()
                    reports = item.reports.toString()
                    course = item.course.toString()

                    binding.tvMyChangeInfoId.setText(userID)
                    binding.tvMyChangeInfoBirth.setText(date)
                    binding.tvMyChangeInfoName.setText(userName)
                    binding.tvMyChangeInfoCompany.setText(company)
                    get_category(item.food.toString(), item.travel.toString(), item.sleep.toString(),
                        item.reports.toString(), item.course.toString())

                }
            }

        binding.btnMyChangeInfoPassword.setOnClickListener {
            resetPassword(userID)
        }

        // 카테고리 받아오기
        binding.rgMyChangeInfoFood.setOnCheckedChangeListener { group, checkId ->
            food = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgMyChangeInfoTravel.setOnCheckedChangeListener{ group, checkId ->
            travel = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgMyChangeInfoSleep.setOnCheckedChangeListener{ group, checkId ->
            sleep = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgMyChangeInfoReports.setOnCheckedChangeListener{ group, checkId ->
            reports = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgMyChangeInfoCourse.setOnCheckedChangeListener{ group, checkId ->
            course = findViewById<RadioButton>(checkId).text.toString()
        }

        binding.btMyChangeInfoDone.setOnClickListener {
             if(binding.tvMyChangeInfoName.text.toString().isEmpty()){
                Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else if(food.isEmpty()){
                 Toast.makeText(this, "선호 음식점을 선택해주세요", Toast.LENGTH_LONG).show()

             }
            else if(travel.isEmpty()){
                 Toast.makeText(this, "선호 관광지를 선택해주세요", Toast.LENGTH_LONG).show()

            }
            else if(sleep.isEmpty()){
                 Toast.makeText(this, "선호 숙소를 선택해주세요", Toast.LENGTH_LONG).show()

            }
            else if(reports.isEmpty()){
                 Toast.makeText(this, "선호 레포츠를 선택해주세요", Toast.LENGTH_LONG).show()

            }
            else if(course.isEmpty()) {
                 Toast.makeText(this, "선호 코스를 선택해주세요", Toast.LENGTH_LONG).show()
            }
            else{
                var userInfo = UserBaseData( // 데이터 구조
                    userID = binding.tvMyChangeInfoId.text.toString(),
                    userName = binding.tvMyChangeInfoName.text.toString(),
                    date = binding.tvMyChangeInfoBirth.text.toString(),
                    company = binding.tvMyChangeInfoCompany.text.toString(),
                    food = food,
                    travel = travel,
                    sleep = sleep,
                    reports = reports,
                    course = course
                )

                db?.collection("user")
                    ?.document(auth?.uid.toString())
                    ?.set(userInfo)


                // 이름 저장
                SocketApplication.prefs.setString("user-name", userName)


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

    // 비밀번호 재설정
    private fun resetPassword(email:String){
        auth?.sendPasswordResetEmail(email)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "메일을 확인해주세요", Toast.LENGTH_LONG).show()
                }
            }
            ?.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()

            }
    }

    fun get_category(item_food:String, item_travel:String, item_sleep:String,
                     item_reports:String, item_course:String){
        when(item_food){
            binding.rbMyChangeInfoFood1.text.toString() ->{
                binding.rbMyChangeInfoFood1.isChecked  = true
                food = binding.rbMyChangeInfoFood1.text.toString()
            }

            binding.rbMyChangeInfoFood2.text.toString() ->{
                binding.rbMyChangeInfoFood2.isChecked  = true
                food = binding.rbMyChangeInfoFood2.text.toString()
            }

            binding.rbMyChangeInfoFood3.text.toString() ->{
                binding.rbMyChangeInfoFood3.isChecked  = true
                food = binding.rbMyChangeInfoFood3.text.toString()
            }

            binding.rbMyChangeInfoFood4.text.toString() ->{
                binding.rbMyChangeInfoFood4.isChecked  = true
                food = binding.rbMyChangeInfoFood4.text.toString()
            }

            binding.rbMyChangeInfoFood5.text.toString() ->{
                binding.rbMyChangeInfoFood5.isChecked  = true
                food = binding.rbMyChangeInfoFood5.text.toString()
            }

            binding.rbMyChangeInfoFood6.text.toString() ->{
                binding.rbMyChangeInfoFood6.isChecked  = true
                food = binding.rbMyChangeInfoFood6.text.toString()
            }

        }

        when(item_travel){
            binding.rbMyChangeInfoTravel1.text.toString() ->{
                binding.rbMyChangeInfoTravel1.isChecked  = true
                travel = binding.rbMyChangeInfoTravel1.text.toString()
            }

            binding.rbMyChangeInfoTravel2.text.toString() ->{
                binding.rbMyChangeInfoTravel2.isChecked  = true
                travel = binding.rbMyChangeInfoTravel2.text.toString()
            }

            binding.rbMyChangeInfoTravel3.text.toString() ->{
                binding.rbMyChangeInfoTravel3.isChecked  = true
                travel = binding.rbMyChangeInfoTravel3.text.toString()
            }

            binding.rbMyChangeInfoTravel4.text.toString() ->{
                binding.rbMyChangeInfoTravel4.isChecked  = true
                travel = binding.rbMyChangeInfoTravel4.text.toString()
            }

            binding.rbMyChangeInfoTravel5.text.toString() ->{
                binding.rbMyChangeInfoTravel5.isChecked  = true
                travel = binding.rbMyChangeInfoTravel5.text.toString()
            }

            binding.rbMyChangeInfoTravel6.text.toString() ->{
                binding.rbMyChangeInfoTravel6.isChecked  = true
                travel = binding.rbMyChangeInfoTravel6.text.toString()
            }


            binding.rbMyChangeInfoTravel7.text.toString() ->{
                binding.rbMyChangeInfoTravel7.isChecked  = true
                travel = binding.rbMyChangeInfoTravel7.text.toString()
            }

        }

        when(item_sleep){
            binding.rbMyChangeInfoSleep1.text.toString() ->{
                binding.rbMyChangeInfoSleep1.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep1.text.toString()
            }

            binding.rbMyChangeInfoSleep2.text.toString() ->{
                binding.rbMyChangeInfoSleep2.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep2.text.toString()
            }

            binding.rbMyChangeInfoSleep3.text.toString() ->{
                binding.rbMyChangeInfoSleep3.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep3.text.toString()
            }

            binding.rbMyChangeInfoSleep4.text.toString() ->{
                binding.rbMyChangeInfoSleep4.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep4.text.toString()
            }

            binding.rbMyChangeInfoSleep5.text.toString() ->{
                binding.rbMyChangeInfoSleep5.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep5.text.toString()
            }

            binding.rbMyChangeInfoSleep6.text.toString() ->{
                binding.rbMyChangeInfoSleep6.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep6.text.toString()
            }


            binding.rbMyChangeInfoSleep7.text.toString() ->{
                binding.rbMyChangeInfoSleep7.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep7.text.toString()
            }
            binding.rbMyChangeInfoSleep8.text.toString() ->{
                binding.rbMyChangeInfoSleep8.isChecked  = true
                sleep = binding.rbMyChangeInfoSleep8.text.toString()
            }

        }

        when(item_reports) {
            binding.rbMyChangeInfoReports1.text.toString() -> {
                binding.rbMyChangeInfoReports1.isChecked = true
                reports = binding.rbMyChangeInfoReports1.text.toString()
            }

            binding.rbMyChangeInfoReports2.text.toString() -> {
                binding.rbMyChangeInfoReports2.isChecked = true
                reports = binding.rbMyChangeInfoReports2.text.toString()
            }

            binding.rbMyChangeInfoReports3.text.toString() -> {
                binding.rbMyChangeInfoReports3.isChecked = true
                reports = binding.rbMyChangeInfoReports3.text.toString()
            }

            binding.rbMyChangeInfoReports4.text.toString() -> {
                binding.rbMyChangeInfoReports4.isChecked = true
                reports = binding.rbMyChangeInfoReports4.text.toString()
            }

            binding.rbMyChangeInfoReports5.text.toString() -> {
                binding.rbMyChangeInfoReports5.isChecked = true
                reports = binding.rbMyChangeInfoReports5.text.toString()
            }

        }

        when(item_course) {
            binding.rbMyChangeInfoCourse1.text.toString() -> {
                binding.rbMyChangeInfoCourse1.isChecked = true
                course = binding.rbMyChangeInfoCourse1.text.toString()
            }

            binding.rbMyChangeInfoCourse2.text.toString() -> {
                binding.rbMyChangeInfoCourse2.isChecked = true
                course = binding.rbMyChangeInfoCourse2.text.toString()
            }

            binding.rbMyChangeInfoCourse3.text.toString() -> {
                binding.rbMyChangeInfoCourse3.isChecked = true
                course = binding.rbMyChangeInfoCourse3.text.toString()
            }

            binding.rbMyChangeInfoCourse4.text.toString() -> {
                binding.rbMyChangeInfoCourse4.isChecked = true
                course = binding.rbMyChangeInfoCourse4.text.toString()
            }

            binding.rbMyChangeInfoCourse5.text.toString() -> {
                binding.rbMyChangeInfoCourse5.isChecked = true
                course = binding.rbMyChangeInfoCourse5.text.toString()
            }

        }

    }
}