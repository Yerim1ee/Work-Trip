package com.example.worktrip.Plan

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.MainActivity
import com.example.worktrip.Plan.Adapter.PlanWorkshopAdapter
import com.example.worktrip.databinding.FragmentPlanTabMain02Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Plan_tab_main01Fragment : Fragment() {

    private lateinit var binding: FragmentPlanTabMain02Binding

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val itemList = mutableListOf<PlanWorkShopData>()
    val adapter = PlanWorkshopAdapter(MainActivity(),itemList)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanTabMain02Binding.inflate(inflater, container, false)

        binding.rcvPlanMain2Recyclerview.layoutManager = LinearLayoutManager(context)
        binding.rcvPlanMain2Recyclerview.adapter= adapter






        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        // 오늘 날짜 받아오기
        // 게시글 now, past 분류하기 위해 받아옴
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val today = LocalDate.now()
        val todayDate_format = today.format(formatter) // 형식 지정
        val todayDate: LocalDate = LocalDate.parse(todayDate_format, formatter)

        // 현재의 게시물인지 확인 후 속성 변경
        db.collection("workshop")
            .whereEqualTo("now",true)
            .get()
            .addOnSuccessListener { result -> // 성공
                for (document in result) {
                    val item = document.toObject(PlanWorkShopData::class.java)

                    val endDate: LocalDate = LocalDate.parse(item.tv_plan_date_end.toString(), formatter)

                    if(endDate.isBefore(todayDate)){
                        db.collection("workshop")
                            .document(document.id)
                            .update("now", false)
                    }
                }}

        // 자신의 uid -> workshop-list 체크
        db.collection("user_workshop")
            .document(auth.uid.toString())
            .collection("workshop_list")
            .orderBy("start_date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (document in result) {
                    db.collection("workshop")
                        .document(document.id)
                        .get()
                        .addOnSuccessListener {document_workshop->
                            // 리사이클러 뷰 아이템 설정 및 추가
                            val item = document_workshop.toObject(PlanWorkShopData::class.java)
                            if (item != null) {
                                if(item.now){
                                    itemList.add(item)
                                }
                            }
                            adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
                        }
                        .addOnFailureListener {

                        }
                }
            }
            .addOnFailureListener {
            }

    }


}