package com.example.worktrip.SignUp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.worktrip.databinding.ActivitySignUp04Binding

class SignUpActivity04 : Activity() {

    // 뷰 바인딩을 위한 객체 획득
    private lateinit var binding: ActivitySignUp04Binding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUp04Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var name = intent.getStringExtra("name").toString()
        binding.tvSignupNameCheck.setText(name)

        binding.btSingupStart.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)

        }

    }


}