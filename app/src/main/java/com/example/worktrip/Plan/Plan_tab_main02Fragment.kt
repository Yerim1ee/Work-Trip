package com.example.worktrip.Plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.MainActivity
import com.example.worktrip.Plan.Adapter.PlanWorkshopAdapter
import com.example.worktrip.databinding.FragmentPlanTabMain02Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


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


        return binding.root
    }

    override fun onResume() {
        super.onResume()

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
                                if(!(item.now)){
                                    itemList.add(item)
                                    Log.d("lee", itemList.toString())
                                }
                            }
                            adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
                        }
                        .addOnFailureListener { exception -> // 실패
                            Log.d("lee", "Error getting documents: ", exception)

                        }
                }
            }
            .addOnFailureListener { exception -> // 실패
                Log.d("lee", "Error getting documents: ", exception)
            }
    }


}