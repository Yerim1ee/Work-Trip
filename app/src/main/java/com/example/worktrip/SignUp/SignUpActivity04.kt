package com.example.worktrip.SignUp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.ActivitySignUp04Binding

class SignUpActivity04 : Activity() {

    // 뷰 바인딩을 위한 객체 획득
    private lateinit var binding: ActivitySignUp04Binding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUp04Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이름 가져오기
        var name = SocketApplication.prefs.getString("user-name", "익명")
        binding.tvSignupNameCheck.setText("${name} 님")

        binding.btSingupStart.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)

        }

    }


}