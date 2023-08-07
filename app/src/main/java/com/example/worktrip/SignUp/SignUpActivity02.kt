package com.example.worktrip.SignUp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.worktrip.databinding.ActivitySignUp02Binding

class SignUpActivity02 : Activity() {

    // 뷰 바인딩을 위한 객체 획득
    private lateinit var binding: ActivitySignUp02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUp02Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var id = intent.getStringExtra("id")
        var password = intent.getStringExtra("password")


        // 에러 처리
        binding.btSingup2Next.setOnClickListener{
            var intent: Intent = Intent(this, SignUpActivity03::class.java)
            intent.putExtra("id", id)
            intent.putExtra("password", password) // 비밀번호만 넘겨도 괜찮을지...
            intent.putExtra("name", binding.etSignupName.text.toString())
            intent.putExtra("date", binding.etSignupBirth.text.toString())
            intent.putExtra("company",binding.etSignupCompany.text.toString())
            startActivity(intent)
        }
    }
}