package com.example.worktrip.SignUp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.StateSet.TAG
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityLoginBinding
import com.example.worktrip.MainActivity
import com.example.worktrip.SocketApplication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.util.maps.helper.Utility


class LoginActivity :  Activity() {
    // 액티비티에서 사용할 레이아웃의 뷰 바인딩 클래스
    private lateinit var binding: ActivityLoginBinding
    lateinit var db : FirebaseFirestore

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            kakao_firebase_login(token.accessToken)

        }
    }

    // 로그인 인증을 위한 객체 획득
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater) // 뷰 바인딩 클래스의 인스턴스를 생성합니다.
        setContentView(binding.root) // 생성된 뷰를 액티비티에 표시합니다.
        auth = Firebase.auth // auth 정의
        db = FirebaseFirestore.getInstance() // db 정의


        // 로그인 버튼 클릭 시 로그인 정보 확인
        binding.ibLoginLogin.setOnClickListener {
            signin()
        }


        binding.ibLoginSignup.setOnClickListener{
            val nextIntent = Intent(this, SignUpActivity01::class.java)
            startActivity(nextIntent)
        }



        binding.tvMissingPassword.setOnClickListener {
            // Dialog만들기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog01, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val  mAlertDialog = mBuilder.show()

            val okButton = mDialogView.findViewById<ImageButton>(R.id.ib_dialog_done)
            okButton.setOnClickListener {
                resetPassword(mDialogView.findViewById<EditText>(R.id.et_dialog_email).text.toString())
            }

            val noButton = mDialogView.findViewById<ImageButton>(R.id.ib_dialog_back)
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }


    }

    public override fun onStart() {
        super.onStart()
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

    // 로그인 시에 불러 로그인되도록 하는 함수
    fun signin(){
        if (binding.etLoginId.text.toString().isNotEmpty() && binding.etLoginPassword.text.toString().isNotEmpty()) {
            auth?.signInWithEmailAndPassword(binding.etLoginId.text.toString(), binding.etLoginPassword.text.toString())
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    // 로그인 성공 시 메인 액티비티로 넘어가주는 함수
    fun moveMainPage(user: FirebaseUser?){
        if(user !=null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun kakao_firebase_login(accessToken: String){

        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("aaa", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                var name = user.kakaoAccount?.profile?.nickname
                var kakao_number = user.id

                auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // 데이터 베이스에 카카오 넘버가 있는지 검사하기

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success")
                            var userInfo = UserBaseData() // 데이터 구조
                            userInfo.userID = auth?.uid
                            userInfo.userName = name
                            userInfo.kakaonumber = kakao_number.toString()

                            db?.collection("user")
                                ?.document(auth?.uid.toString())
                                ?.set(userInfo)
                            startActivity(Intent(this, MainActivity::class.java))

                            // 이름 저장
                            SocketApplication.prefs.setString("user-name", name.toString())

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
    }

}