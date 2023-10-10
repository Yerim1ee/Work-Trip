package com.worktrip.SignUp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.worktrip.MainActivity
import com.worktrip.R
import com.worktrip.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.worktrip.DataClass.UserBaseData
import com.worktrip.DataClass.uidData
import com.worktrip.SocketApplication


class LoginActivity :  Activity() {
    // 액티비티에서 사용할 레이아웃의 뷰 바인딩 클래스
    private lateinit var binding: ActivityLoginBinding
    lateinit var db : FirebaseFirestore


    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨


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
            // Dialog만들기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog08, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val callback_sign_up: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                } else if (token != null) {
                    kakao_firebase_sign_up()
                }
            }
            val  mAlertDialog = mBuilder.show()

            val okButton = mDialogView.findViewById<Button>(R.id.btn_dialog_sign_up_basic)
            okButton.setOnClickListener {
                mAlertDialog.dismiss()
                val nextIntent = Intent(this, SignUpActivity01::class.java)
                startActivity(nextIntent)
            }

            val noButton = mDialogView.findViewById<Button>(R.id.btn_dialog_sign_up_kakao)
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                    UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                        if (error != null) {
                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }

                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback_sign_up)
                        } else if (token != null) {
                            kakao_firebase_sign_up()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback_sign_up)
                }
            }
        }

        binding.ibLoginKakaoLogin.setOnClickListener {
            val callback_login: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                } else if (token != null) {
                    kakao_firebase_login()
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback_login)
                    } else if (token != null) {
                        kakao_firebase_login()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback_login)
            }
        }


        var click = false

        binding.ivLoginVisibility.setOnClickListener{
            click = !click
            if(click){
                binding.etLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT)
            }
            else{
                binding.etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)
            }
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
                            baseContext, "로그인에 실패 하였습니다. 정보/인터넷 연결을 확인하세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    // 카카오 signin
    fun kakao_signin(kakao_email:String, kakao_password: String){
            auth?.signInWithEmailAndPassword(kakao_email, kakao_password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다. 정보/인터넷 연결을 확인하세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ?.addOnFailureListener{

                }
        }

    // 로그인 성공 시 메인 액티비티로 넘어가주는 함수
    fun moveMainPage(user: FirebaseUser?){
        if(user !=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    fun kakao_firebase_sign_up(){
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
            }
            else if (user != null) {
                var name = user.kakaoAccount?.profile?.nickname
                var kakao_number = user.id

                db.collection("user")
                    .whereEqualTo("kakaonumber", kakao_number.toString())
                    .get()
                    .addOnSuccessListener {
                            result ->
                        if(result.isEmpty){
                                var userInfo = UserBaseData(
                                    "${kakao_number}@example.com",
                                    null,
                                    kakao_number.toString(),
                                    name,
                                    "",
                                    "",
                                    "한식",
                                    "자연관광지",
                                    "한옥",
                                    "선호X",
                                    "힐링 코스"
                                ) // 데이터 구조
                                userInfo.userName = name
                                userInfo.kakaonumber = kakao_number.toString()
                                sign_up("${kakao_number}@example.com", kakao_number.toString(), userInfo )


                        }
                        else{
                                Toast.makeText(this, "이미 카카오로 회원가입을 진행하셨습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {

                    }

            }
        }

    }

    fun kakao_firebase_login(){
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
            } else if (user != null) {
                var name = user.kakaoAccount?.profile?.nickname
                var kakao_number = user.id

                db.collection("user")
                    .whereEqualTo("kakaonumber", kakao_number.toString())
                    .get()
                    .addOnSuccessListener {
                        result ->
                        if(!(result.isEmpty)){
                            kakao_signin("${kakao_number}@example.com", kakao_number.toString())
                        }
                    }
            }
        }
    }

    fun sign_up(id:String, password:String, userInfo:UserBaseData){
        // createUserWithEmailAndPassword() 이용하여 사용자 생성
        auth.createUserWithEmailAndPassword(id, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 생성이 되었다면

                    db?.collection("user")
                        ?.document(auth?.uid.toString())
                        ?.set(userInfo)

                    // 이름 저장
                    SocketApplication.prefs.setString("user-name", userInfo.userName.toString())
                    var user_id = uidData(
                        auth.uid.toString()
                    )

                    // 자신의 id에 추가하기
                    db.collection("user_workshop")
                        .document(auth.uid.toString())
                        .set(user_id)

                    Toast.makeText(this, "카카오 회원가입 성공", Toast.LENGTH_LONG).show()

                } else { // 생성을 못했다면
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG)
                        .show() // 토스트 메세지 띄우기
                    //Show the error message
                }
            }
            .addOnFailureListener {e ->


            }
    }

}