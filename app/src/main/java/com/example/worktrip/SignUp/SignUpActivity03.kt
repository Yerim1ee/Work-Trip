package com.example.worktrip.SignUp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivitySignUp03Binding
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.DataClass.uidData
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
    lateinit var db: FirebaseFirestore

    // 체크박스 체크 표시를 위한 boolean array
     var food: String = "한식"
     var travel: String = "자연관광지"
     var sleep: String = "한옥"
     var reports: String = "선호 X"
     var course: String = "힐링 코스"

    lateinit var id: String
    lateinit var password: String
    lateinit var name: String
    lateinit var date: String
    lateinit var company: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp03Binding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        password = intent.getStringExtra("password").toString()
        name = intent.getStringExtra("name").toString()
        date = intent.getStringExtra("date").toString()
        company = intent.getStringExtra("company").toString()


        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        var user_id = uidData(
            auth.uid.toString()
        )

        // 카테고리 받아오기
        binding.rgPlanSignup3Food.setOnCheckedChangeListener { group, checkId ->
            food = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgPlanSignup3Travel.setOnCheckedChangeListener{ group, checkId ->
            travel = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgPlanSignup3Sleep.setOnCheckedChangeListener{ group, checkId ->
            sleep = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgPlanSignup3Reports.setOnCheckedChangeListener{ group, checkId ->
            reports = findViewById<RadioButton>(checkId).text.toString()
        }
        binding.rgPlanSignup3Course.setOnCheckedChangeListener{ group, checkId ->
           course = findViewById<RadioButton>(checkId).text.toString()
        }

        // 자신의 id에 추가하기
        db.collection("user_workshop")
            .document(auth.uid.toString())
            .set(user_id)

        // 에러 처리
        binding.btSingup3Next.setOnClickListener {
            signUpEmail()
        }
    }


    fun signUpEmail() {

        // createUserWithEmailAndPassword() 이용하여 사용자 생성
        auth.createUserWithEmailAndPassword(id, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 생성이 되었다면
                    var userInfo = UserBaseData( // 데이터 구조
                        userID = auth?.uid,
                        userName = name,
                        date = date,
                        company = company,
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
                    SocketApplication.prefs.setString("user-name", name)


                    // 메인으로 이동
                    val nextIntent = Intent(this, SignUpActivity04::class.java)
                    startActivity(nextIntent)
                } else { // 생성을 못했다면
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG)
                        .show() // 토스트 메세지 띄우기
                    //Show the error message
                }
            }
    }
}