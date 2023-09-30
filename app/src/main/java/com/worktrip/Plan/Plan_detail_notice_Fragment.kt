package com.worktrip.Plan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.worktrip.DataClass.PlanNoticeData
import com.worktrip.DataClass.PlanWorkShopUserData
import com.worktrip.MainActivity
import com.worktrip.Plan.Adapter.Plan_detail_notice_Adapter
import com.worktrip.SocketApplication
import com.worktrip.databinding.FragmentPlanDetailNoticeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Plan_detail_notice_Fragment : Fragment() {

    private lateinit var binding: FragmentPlanDetailNoticeBinding

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val itemList = mutableListOf<PlanNoticeData>()
    val adapter = Plan_detail_notice_Adapter(MainActivity(),itemList)

    lateinit var workshop_docID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanDetailNoticeBinding.inflate(inflater, container, false)


        // 리사이클러뷰 연결
        binding.rcvPlanDetailNoticeRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.rcvPlanDetailNoticeRecyclerview.adapter= adapter

        // workshop 게시글 번호 받아오기
        workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")
        //// 다른 루트에서 오지 않을 경우 받을 수 있는 방법 생각해서 defValue에 넣어두기

        binding.btPlanDetailNoticePlus.setOnClickListener {
            val intent = Intent(activity, Plan_detail_notice_plus_Activity::class.java)
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
                        binding.btPlanDetailNoticePlus.visibility = View.GONE
                        // 초반 리스트 설정
                        firestore_get(item_result.part.toString())
                    }
                    else{
                        binding.btPlanDetailNoticePlus.visibility = View.VISIBLE
                        // 초반 리스트 설정
                        firestore_get(item_result.part.toString())

                    }
                }
            }


        return binding.root
    }

    override fun onResume() {
        super.onResume()


    }
    fun firestore_get(part:String){

        db.collection("workshop")
            .document(workshop_docID)
            .collection("notice")
            .orderBy("docID", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result -> // 성공
                itemList.clear()
                for (document in result) {
                    val item = document.toObject(PlanNoticeData::class.java)
                    item.docID = document.id// 내부적으로 식별할 수 있는 게시물 식별자
                    if(part.equals("참가자")){
                        if(!(item.plan_people.equals("기획자"))){
                                itemList.add(item)
                        }

                    }else{
                        itemList.add(item)
                    }

                    // 리사이클러 뷰 갱신
                    adapter.notifyDataSetChanged()
                }
                if(itemList.isEmpty()){
                    binding.tvPlanDetailNoticeNull.visibility = View.VISIBLE
                }
                else{
                    binding.tvPlanDetailNoticeNull.visibility = View.GONE
                }

            }
            .addOnFailureListener {
            }

    }
}