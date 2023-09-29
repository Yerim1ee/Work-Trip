package com.example.worktrip.Plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityPlanWorkshopPlus2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Plan_workshop_plus2_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanWorkshopPlus2Binding
    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlanWorkshopPlus2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_plus2))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanPlusTitle2
        toolbarTitle.text = "초대 코드 입력"

        binding.btPlanWorkshopPlusDone.setOnClickListener {
            var code = binding.etPlanCode.text.toString()

            db.collection("workshop")
                .document(code)
                .get()
                .addOnSuccessListener {result->
                    val item = result.toObject(PlanWorkShopData::class.java)

                    if (item != null) {
                        db.collection("user")
                            .document(auth.uid.toString())
                            .get()
                            .addOnSuccessListener { result ->
                                val item_user = result.toObject(UserBaseData::class.java)
                                if (item_user != null) {

                                db.collection("user_workshop")
                                    .document(auth.uid.toString())
                                    .collection("workshop_list")
                                    .document(code)
                                    .get()
                                    .addOnSuccessListener {result ->
                                        val item_user_workshop = result.toObject(PlanWorkShopUserData::class.java)
                                        if (item_user_workshop != null) {
                                                Toast.makeText(this, "이미 존재하는 워크숍입니다. 새로운 워크숍을 추가해주세요", Toast.LENGTH_LONG).show()
                                        }else{
                                                var user_workshop_data = PlanWorkShopUserData(
                                                    code,
                                                    item.tv_plan_date_start,
                                                    "참가자",
                                                    auth.uid.toString(),
                                                    item_user.userName
                                                )


                                                db.collection("user_workshop")
                                                    .document(auth.uid.toString())
                                                    .collection("workshop_list")
                                                    .document(code)
                                                    .set(user_workshop_data)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(this, "워크숍이 추가되었습니다.", Toast.LENGTH_LONG).show()
                                                        finish()

                                                    }

                                        }
                                    }
                                }
                            }
                    }
                    else{
                        Toast.makeText(this, "초대 코드가 잘못되었습니다. 다시 입력해주세요", Toast.LENGTH_LONG).show()
                    }
                }

        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}