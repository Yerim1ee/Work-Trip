package com.example.worktrip.SignUp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivitySignUp03Binding
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.MainActivity
import com.example.worktrip.PreferenceUtil
import com.example.worktrip.SocketApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpActivity03 : Activity() {

    // 뷰 바인딩을 위한 객체 획득
    private lateinit var binding: ActivitySignUp03Binding

    // 파이어 베이스 회원가입을 위한 객체 획득
    private lateinit var auth: FirebaseAuth
    var email: String? = null
    lateinit var db : FirebaseFirestore

    // 체크박스 체크 표시를 위한 boolean array
    var food :BooleanArray = BooleanArray(6)
    var travel : BooleanArray = BooleanArray(7)
    var sleep :BooleanArray = BooleanArray(9)
    var reports : BooleanArray = BooleanArray(5)
    var course : BooleanArray = BooleanArray(5)

    lateinit var id :String
    lateinit var password:String
    lateinit var name :String
    lateinit var date:String
    lateinit var company:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUp03Binding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        password = intent.getStringExtra("password").toString()
        name = intent.getStringExtra("name").toString()
        date = intent.getStringExtra("date").toString()
        company = intent.getStringExtra("company").toString()


        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth



        // 에러 처리
        binding.btSingup3Next.setOnClickListener{
            signUpEmail()
        }
    }


    fun signUpEmail(){
        // createUserWithEmailAndPassword() 이용하여 사용자 생성
        auth.createUserWithEmailAndPassword(id,password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){ // 생성이 되었다면
                    var userInfo = UserBaseData() // 데이터 구조
                    userInfo.userID = auth?.uid
                    userInfo.userName = name
                    userInfo.date = date
                    userInfo.company = company
                    // userInfo.food = food
                    // userInfo.travel = travel
                    //  userInfo.sleep = sleep
                    //  userInfo.reports = reports
                    //   userInfo.course = course

                    db?.collection("user")
                        ?.document(auth?.uid.toString())
                        ?.set(userInfo)

                    // 이름 저장
                    SocketApplication.prefs.setString("user-name", name)


                    // 메인으로 이동
                    val nextIntent = Intent(this, SignUpActivity04::class.java)
                    startActivity(nextIntent)
                }
                else{ // 생성을 못했다면
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show() // 토스트 메세지 띄우기
                    //Show the error message
                }
            }
    }
    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {

                // food 트랙
                R.id.thema1 -> {
                    food[0] = checked
                }
                R.id.thema2 -> {
                    food[1] = checked
                }
                R.id.thema3 -> {
                    food[2] = checked
                }
                R.id.thema4 -> {
                    food[3] = checked
                }
                R.id.thema5 -> {
                    food[4] = checked
                }
                R.id.thema6 -> {
                    food[5] = checked
                }

                // travel 트랙
                R.id.cb_travel_thema1 -> {
                    travel[0] = checked
                }
                R.id.cb_travel_thema2 -> {
                    travel[1] = checked
                }
                R.id.cb_travel_thema3 -> {
                    travel[2] = checked
                }
                R.id.cb_travel_thema4 -> {
                    travel[3] = checked
                }
                R.id.cb_travel_thema5 -> {
                    travel[4] = checked
                }
                R.id.cb_travel_thema6 -> {
                    travel[5] = checked
                }
                R.id.cb_travel_thema7 -> {
                    travel[6] = checked
                }


                // sleep 트랙
                R.id.cb_sleep_thema1 -> {
                    sleep[0] = checked
                }
                R.id.cb_sleep_thema2 -> {
                    sleep[1] = checked
                }
                R.id.cb_sleep_thema3 -> {
                    sleep[2] = checked
                }
                R.id.cb_sleep_thema4 -> {
                    sleep[3] = checked
                }
                R.id.cb_sleep_thema5 -> {
                    sleep[4] = checked
                }
                R.id.cb_sleep_thema6 -> {
                    sleep[5] = checked
                }
                R.id.cb_sleep_thema7 -> {
                    sleep[6] = checked
                }
                R.id.cb_sleep_thema8 -> {
                    sleep[7] = checked
                }
                R.id.cb_sleep_thema9 -> {
                    sleep[8] = checked
                }


                // reports 트랙
                R.id.cb_reports_thema1 -> {
                    reports[0] = checked
                }
                R.id.cb_reports_thema2 -> {
                    reports[1] = checked
                }
                R.id.cb_reports_thema3 -> {
                    reports[2] = checked
                }
                R.id.cb_reports_thema4 -> {
                    reports[3] = checked
                }
                R.id.cb_reports_thema5 -> {
                    reports[4] = checked
                }

                // course 트랙
                R.id.cb_course_thema1 -> {
                    course[0] = checked
                }
                R.id.cb_course_thema2 -> {
                    course[1] = checked
                }
                R.id.cb_course_thema3 -> {
                    course[2] = checked
                }
                R.id.cb_course_thema4 -> {
                    course[3] = checked
                }
                R.id.cb_course_thema5 -> {
                    course[4] = checked
                }
            }
        }
    }

}

