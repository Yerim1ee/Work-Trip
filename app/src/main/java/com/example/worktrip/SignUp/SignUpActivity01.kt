package com.example.worktrip.SignUp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.databinding.ActivitySignUp01Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpActivity01 : Activity() {


    var email: Boolean = false
    var password: Boolean = false
    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // 뷰 바인딩을 위한 객체 획득
    private lateinit var binding: ActivitySignUp01Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignUp01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etSignupId.addTextChangedListener(textWatcher)
        binding.etSignupRePassword.addTextChangedListener(textWatcher)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        binding.btSingup1Next.setOnClickListener{

            if(binding.etSignupId.text.toString().isEmpty())
            {
                Toast.makeText(this, "아이디를 입력해주세요.",Toast.LENGTH_LONG).show()

            }
            else if(binding.tvSignupIdError.text.toString().equals("이메일 형식에 맞지 않습니다.")){
                Toast.makeText(this, "이메일 형식에 맞지 않습니다.",Toast.LENGTH_LONG).show()

            }
            else if(binding.tvSignupRePasswordError.text.toString().equals("비밀번호를 일치시켜주세요")){
                Toast.makeText(this, "비밀번호를 일치시켜주세요",Toast.LENGTH_LONG).show()
            }

            else if(binding.etSignupPassword.text.toString().length < 6){
                Toast.makeText(this, "6자 이상 입력해주세요..",Toast.LENGTH_LONG).show()

            }

            else if(binding.tvSignupPasswordError.equals("6자 이상 입력해주세요.")){
                Toast.makeText(this, "6자 이상 입력해주세요..",Toast.LENGTH_LONG).show()

            }
            else{

                        if(!(binding.tvSignupIdError.text.toString().equals("해당 아이디가 존재합니다."))){
                            var intent: Intent = Intent(this, SignUpActivity02::class.java)
                            intent.putExtra("id", binding.etSignupId.text.toString())
                            intent.putExtra("password", binding.etSignupPassword.text.toString())
                            startActivity(intent)
                        }
            }



        }



    }

    //정규식 활용 이메일 형식 체크
    fun isEmailValid(email: String): Boolean {
        var isValid = false
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }


    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            if(!(binding.etSignupPassword.text.toString().equals(binding.etSignupRePassword.text.toString()))){
                binding.tvSignupRePasswordError.setVisibility(View.VISIBLE)
                binding.tvSignupRePasswordError.setText("비밀번호를 일치시켜주세요")
            }
            else{
                password = true
                binding.tvSignupRePasswordError.setVisibility(View.VISIBLE)
                binding.tvSignupRePasswordError.setText("비밀번호가 일치합니다.")
            }


            // 입력하기 전에 조치

            if(isEmailValid(binding.etSignupId.text.toString())){
                binding.tvSignupIdError.setVisibility(View.VISIBLE)
                binding.tvSignupIdError.setText("이메일 형식에 맞습니다.")
            }
            else{
                email = true
                binding.tvSignupIdError.setVisibility(View.VISIBLE)
                binding.tvSignupIdError.setText("이메일 형식에 맞지 않습니다.")
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // 입력란에 변화가 있을 시 조치

            if(!(binding.etSignupPassword.text.toString().equals(binding.etSignupRePassword.text.toString()))){
                binding.tvSignupRePasswordError.setVisibility(View.VISIBLE)
                binding.tvSignupRePasswordError.setText("비밀번호를 일치시켜주세요")
            }
            else{
                password = true
                binding.tvSignupRePasswordError.setVisibility(View.VISIBLE)
                binding.tvSignupRePasswordError.setText("비밀번호가 일치합니다.")
            }


            // 입력하기 전에 조치

            if(isEmailValid(binding.etSignupId.text.toString())){
                binding.tvSignupIdError.setVisibility(View.VISIBLE)
                binding.tvSignupIdError.setText("이메일 형식에 맞습니다.")
            }
            else{
                email = true
                binding.tvSignupIdError.setVisibility(View.VISIBLE)
                binding.tvSignupIdError.setText("이메일 형식에 맞지 않습니다.")
            }

            if(binding.etSignupPassword.text.toString().length < 6){
                binding.tvSignupPasswordError.setText("6자 이상 입력해주세요.")
            }
        }

        override fun afterTextChanged(s: Editable) {
            // 입력이 끝났을 때 조치
            db.collection("user")
                .get()
                .addOnSuccessListener {
                    result ->
                    for (document in result){
                        var item = document.toObject(UserBaseData::class.java)
                        if(item.userID.equals(binding.etSignupId.text.toString())){
                            binding.tvSignupIdError.setText("해당 아이디가 존재합니다.")
                        }
                    }
                }
        }
    }
}