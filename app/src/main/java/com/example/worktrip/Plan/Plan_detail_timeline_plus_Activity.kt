package com.example.worktrip.Plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.worktrip.DataClass.PlanDetailDateData
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.ActivityPlanDetailTimelinePlusBinding
import com.example.worktrip.databinding.ActivityPlanWorkshopPlusBinding
import com.example.worktrip.databinding.FragmentPlanDetailTimelineBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class Plan_detail_timeline_plus_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailTimelinePlusBinding


    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailTimelinePlusBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        // workshop 게시글 번호 받아오기
        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")
        var timeline_docId = SocketApplication.prefs.getString("now_timeline_date", "") //

        binding.etPlanDetailTimelinePlusTitle.setText(intent.getStringExtra("title"))
        binding.tvPlanDetailTimelinePlusPlace.setText(intent.getStringExtra("place"))

        binding.btPlanPlusDone.setOnClickListener {
            if (binding.etPlanDetailTimelinePlusTitle.text.toString().isEmpty()) {
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.tvPlanDetailTimelinePlusTime.text.toString().isEmpty()) {
                Toast.makeText(this, "시간을 설정해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.tvPlanDetailTimelinePlusPlace.text.toString().isEmpty()) {
                Toast.makeText(this, "장소를 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.tvPlanDetailTimelinePlusPresenter.text.toString().isEmpty()) {
                Toast.makeText(this, "진행자를 입력해주세요", Toast.LENGTH_LONG).show()
            }  else {

                var datetimelineData = PlanTimeLineData(
                    timeline_docId, // 타임라인의 아이디 만들어주기
                    binding.etPlanDetailTimelinePlusTitle.text.toString(),
                    binding.tvPlanDetailTimelinePlusPresenter.text.toString(),
                    binding.tvPlanDetailTimelinePlusPlace.text.toString(),
                    binding.tvPlanDetailTimelinePlusTime.text.toString(),
                    binding.tvPlanDetailTimelinePlusTime.text.toString(),

                ) // 데이터 구조


                // workshop 문서 생성
                db.collection("user_workshop")
                    .document("${auth.currentUser?.uid.toString()}")
                    .collection("workshop")
                    .document(workshop_docID)
                    .collection("date")
                    .document(timeline_docId)
                    .collection("timeline")
                    .document()
                    .set(datetimelineData)


                finish()
            }

            }
    }
}