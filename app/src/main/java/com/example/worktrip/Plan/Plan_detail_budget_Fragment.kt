package com.example.worktrip.Plan

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.MainActivity
import com.example.worktrip.My.BookmarkActivity
import com.example.worktrip.Plan.Adapter.Plan_detail_budget_Adapter
import com.example.worktrip.Plan.Adapter.Plan_detail_timeline_Adapter
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.BudgetEditDialogBinding
import com.example.worktrip.databinding.FragmentPlanDetailBudgetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.DecimalFormat
import java.time.LocalDate

class Plan_detail_budget_Fragment : Fragment() {
    private lateinit var binding: FragmentPlanDetailBudgetBinding

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    val t_dec_up = DecimalFormat("#,###")
    val itemList = mutableListOf<PlanBudgetData>()
    val adapter = Plan_detail_budget_Adapter(MainActivity(),itemList)

    lateinit var workshop_docID: String
    var used_price:Int = 0
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

        binding.planDetailBudgetFirstEdit.setOnClickListener {
            // Dialog만들기
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.budget_edit_dialog, null)
            val et_budget = mDialogView.findViewById<EditText>(R.id.ed_dialog_budget)
            val ib_back = mDialogView.findViewById<ImageButton>(R.id.ib_dialog_budget_back)
            val ib_done =  mDialogView.findViewById<ImageButton>(R.id.ib_dialog_budget_done)

            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)

            val  mAlertDialog = mBuilder.show()


            /*

            // 돈 형식에서 int로 변환할 수 있도록 준비
            var str_change_used = t_dec_up.parse(binding.tvPlanDetailBudgetFirst.text.toString())

            et_budget.setText(str_change_used.toInt().toString())

            ib_done.setOnClickListener {
                Log.d("aaaa", et_budget.text.toString())

                var budget = t_dec_up.format(et_budget.text.toString())
                et_budget.setText(budget)

                db.collection("workshop")
                    .document(workshop_docID)
                    .update("tv_plan_budget",budget)
                    .addOnSuccessListener {
                        Log.d("aaaa", budget)

                    }

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
                        mAlertDialog.dismiss()
                    }
            }

            ib_back.setOnClickListener {
                mAlertDialog.dismiss()
            }
             */
        }


        return binding.root


    }


    fun firestore_get(){

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

            }
            .addOnFailureListener { exception -> // 실패
                Log.d("lee", "Error getting documents: ", exception)
            }
    }
}