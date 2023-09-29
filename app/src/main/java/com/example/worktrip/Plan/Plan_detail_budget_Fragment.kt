package com.example.worktrip.Plan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.MainActivity
import com.example.worktrip.Plan.Adapter.Plan_detail_budget_Adapter
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.FragmentPlanDetailBudgetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class Plan_detail_budget_Fragment : Fragment() {
    private lateinit var binding: FragmentPlanDetailBudgetBinding

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth

    val t_dec_up = DecimalFormat("#,###")
    val itemList = mutableListOf<PlanBudgetData>()
    val adapter = Plan_detail_budget_Adapter(MainActivity(),itemList)

    lateinit var workshop_docID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanDetailBudgetBinding.inflate(inflater, container, false)


        // 리사이클러뷰 연결
        binding.rcvPlanDetailBudgetRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.rcvPlanDetailBudgetRecyclerview.adapter= adapter

        // workshop 게시글 번호 받아오기
        workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")
        //// 다른 루트에서 오지 않을 경우 받을 수 있는 방법 생각해서 defValue에 넣어두기

        binding.btPlanDetailBudgetPlus.setOnClickListener {
            val intent = Intent(activity, Plan_detail_budget_plus_Activity::class.java)
            startActivity(intent)
        }



        db.collection("user_workshop")
            .document(auth.uid.toString())
            .collection("workshop_list")
            .document(workshop_docID)
            .get()
            .addOnSuccessListener {
                    result -> // 성공
                val item_result = result.toObject(PlanWorkShopUserData::class.java)
                if (item_result != null) {
                    if(item_result.part.toString().equals("참가자")){
                        binding.btPlanDetailBudgetPlus.visibility = View.GONE
                    }
                    else{
                        binding.btPlanDetailBudgetPlus.visibility = View.VISIBLE

                    }
                }
            }

        return binding.root


    }

    override fun onResume() {
        super.onResume()
        // 초반 예산 내용 설정
        db.collection("workshop")
            .document(workshop_docID)
            .get()
            .addOnSuccessListener {
                    result -> // 성공
                val item = result.toObject(PlanWorkShopData::class.java)
                if (item != null) {
                    binding.tvPlanDetailBudgetFirst.setText(item.tv_plan_budget)
                    binding.tvPlanDetailBudgetLast.setText(item.tv_plan_budget)
                }

                // 초반 리스트 설정
                firestore_get()
            }
    }

    fun firestore_get(){
        var used_price:Int = 0
        db.collection("workshop")
            .document(workshop_docID)
            .collection("budget")
            .orderBy("docID",Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result -> // 성공
                itemList.clear()
                for (document in result) {
                    val item = document.toObject(PlanBudgetData::class.java)
                    item.docID = document.id// 내부적으로 식별할 수 있는 게시물 식별자
                    itemList.add(item)

                    // 돈 형식에서 int로 변환할 수 있도록 준비
                    var str_change_used = t_dec_up.parse(item.plan_price)
                    var str_change_total = t_dec_up.parse(binding.tvPlanDetailBudgetFirst.text.toString())

                    // 사용 금액 계산 및 정리
                    used_price = used_price + str_change_used.toInt()
                    binding.tvPlanDetailBudgetUsed.setText(t_dec_up.format(used_price))

                    // 남은 금액 계산 및 정리
                    var last_price = str_change_total.toInt() - used_price
                    binding.tvPlanDetailBudgetLast.setText(t_dec_up.format(last_price))

                    // 리사이클러 뷰 갱신
                    adapter.notifyDataSetChanged()
                }

                if(itemList.isEmpty()){
                    binding.tvPlanDetailBudgetNull.visibility = View.VISIBLE
                }
                else{
                    binding.tvPlanDetailBudgetNull.visibility = View.GONE
                }

            }
            .addOnFailureListener {
            }

    }
}