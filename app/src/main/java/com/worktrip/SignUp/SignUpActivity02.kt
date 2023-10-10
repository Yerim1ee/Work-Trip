package com.worktrip.SignUp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.worktrip.databinding.ActivitySignUp02Binding

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
            if(binding.etSignupName.text.toString().isEmpty()){
                Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else if(!(binding.cbSignup2CheckAge.isChecked)){
                Toast.makeText(this, "만 18세 미만은 가입이 어렵습니다.", Toast.LENGTH_LONG).show()

            }
            else{
                var intent = Intent(this, SignUpActivity03::class.java)
                intent.putExtra("id", id)
                intent.putExtra("password", password)
                intent.putExtra("name", binding.etSignupName.text.toString())
                intent.putExtra("date", binding.etSignupBirth.text.toString())
                intent.putExtra("company",binding.etSignupCompany.text.toString())
                startActivity(intent)
                finish()
            }

        }
    }
}