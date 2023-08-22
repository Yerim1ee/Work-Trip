package com.example.worktrip.Plan

import android.os.Build
import android.os.Bundle
import android.util.Log
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

        // 오늘 날짜 받아오기
        // 게시글 now, past 분류하기 위해 받아옴
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val today = LocalDate.now()
        val todayDate_format = today.format(formatter) // 형식 지정
        val todayDate: LocalDate = LocalDate.parse(todayDate_format, formatter)

        // 현재의 게시물인지 확인 후 속성 변경
        db.collection("user_workshop")
            .document("${auth.currentUser?.uid.toString()}")
            .collection("workshop")
            .whereEqualTo("now",true)
            .get()
            .addOnSuccessListener { result -> // 성공
                for (document in result) {
                    val item = document.toObject(PlanWorkShopData::class.java)

                    val endDate: LocalDate = LocalDate.parse(item.tv_plan_date_end.toString(), formatter)

                    if(endDate.isBefore(todayDate)){
                        db.collection("user_workshop")
                            .document("${auth.currentUser?.uid.toString()}")
                            .collection("now")
                            .document(document.id)
                            .update("now", false)

                    }

                }}


        // 데이터 정렬 후 내부 데이터로 넘기기
        db.collection("user_workshop")
            .document("${auth.currentUser?.uid.toString()}")
            .collection("workshop")
            .whereEqualTo("now",true)
            .get()
            .addOnSuccessListener { result -> // 성공
                itemList.clear()
                for (document in result) {
                    val item = document.toObject(PlanWorkShopData::class.java)
                    item.docID = document.id// 내부적으로 식별할 수 있는 게시물 식별자
                    itemList.add(item)

                    adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
                }
            }
            .addOnFailureListener { exception -> // 실패
                Log.d("lee", "Error getting documents: ", exception)
            }




        return binding.root
    }

}