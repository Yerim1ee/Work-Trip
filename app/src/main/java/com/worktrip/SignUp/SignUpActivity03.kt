package com.worktrip.SignUp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.worktrip.R
import com.worktrip.databinding.ActivitySignUp03Binding
import com.worktrip.DataClass.UserBaseData
import com.worktrip.DataClass.uidData
import com.worktrip.SocketApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpActivity03 : Activity() {

    // 뷰 바인딩을 위한 객체 획득
    private lateinit var binding: ActivitySignUp03Binding

    // 파이어 베이스 회원가입을 위한 객체 획득
    private  var auth: FirebaseAuth = Firebase.auth
    var email: String? = null
     var db: FirebaseFirestore = FirebaseFirestore.getInstance()

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


        // 에러 처리
        binding.btSingup3Next.setOnClickListener {
            // Dialog만들기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog07, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val  mAlertDialog = mBuilder.show()

            val noButton = mDialogView.findViewById<Button>(R.id.btn_dialog_no_7)
            noButton.setOnClickListener {
                    Toast.makeText(this, "개인정보처리방침에 동의를 완료하셔야 회원가입을 완료하실 수 있습니다.", Toast.LENGTH_LONG).show()

            }

            val okButton = mDialogView.findViewById<Button>(R.id.btn_dialog_yes_7)
            okButton.setOnClickListener{
                mAlertDialog.dismiss()
                signUpEmail()
                }

            val link_check = mDialogView.findViewById<TextView>(R.id.tv_custom_dialog_07_link)
            link_check.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/work-trip-c01ab.appspot.com/o/setting%2F%EC%9A%B4%EC%98%81-%EC%A0%95%EC%B1%85.html?alt=media&token=8ebc728d-94fe-49ac-a153-7f626c58232e"))
                startActivity(intent)
                finish()
            }

        }
    }


    fun signUpEmail() {

        // createUserWithEmailAndPassword() 이용하여 사용자 생성
        auth.createUserWithEmailAndPassword(id, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 생성이 되었다면
                    var userInfo = UserBaseData( // 데이터 구조
                        userID = id,
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
                    var user_id = uidData(
                        auth.uid.toString()
                    )

                    // 자신의 id에 추가하기
                    db.collection("user_workshop")
                        .document(auth.uid.toString())
                        .set(user_id)

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