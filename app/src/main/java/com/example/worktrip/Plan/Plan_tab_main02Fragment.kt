package com.example.worktrip.Plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.MainActivity
import com.example.worktrip.databinding.FragmentPlanTabMain02Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Plan_tab_main02Fragment : Fragment() {
    private lateinit var binding: FragmentPlanTabMain02Binding

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val itemList = mutableListOf<PlanWorkShopData>()
    val adapter = PlanWorkshopAdapter(MainActivity(),itemList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanTabMain02Binding.inflate(inflater, container, false)

        binding.rcvPlanMain2Recyclerview.layoutManager = LinearLayoutManager(context)
        binding.rcvPlanMain2Recyclerview.adapter= adapter

        //             .whereEqualTo("userID",auth.uid.toString())
        db.collection("user_workshop")
            .get()
            .addOnSuccessListener { result -> // 성공
                itemList.clear()
                for (document in result) {
                    val item = document.toObject(PlanWorkShopData::class.java)
                    item.setuesrid(document.id) // 내부적으로 식별할 수 있는 게시물 식별자
                    itemList.add(item)

                    Log.d("lee","가나다"+itemList.toString())

                    adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신

                }

               }
            .addOnFailureListener { exception -> // 실패
                Log.d("lee", "Error getting documents: ", exception)
            }

        return binding.root
    }


}