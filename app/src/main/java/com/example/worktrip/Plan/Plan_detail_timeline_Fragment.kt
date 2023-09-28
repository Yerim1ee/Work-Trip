package com.example.worktrip.Plan

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanDetailDateData
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.MainActivity
import com.example.worktrip.My.BookmarkActivity
import com.example.worktrip.Plan.Adapter.PlanWorkshopAdapter
import com.example.worktrip.Plan.Adapter.Plan_detail_timeline_Adapter
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.FragmentPlanDetailNoticeBinding
import com.example.worktrip.databinding.FragmentPlanDetailTimelineBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.card_item_budget.view.ib_plan_card_detail_budget_plus
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class Plan_detail_timeline_Fragment : Fragment() {


    private lateinit var binding: FragmentPlanDetailTimelineBinding

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val itemList = mutableListOf<PlanTimeLineData>()
    val adapter = Plan_detail_timeline_Adapter(MainActivity(),itemList)

    lateinit var startdate:LocalDate
    lateinit var enddate:LocalDate


    lateinit var workshop_docID: String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanDetailTimelineBinding.inflate(inflater, container, false)

        binding.rcvPlanDetailTimelineRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.rcvPlanDetailTimelineRecyclerview.adapter= adapter

        // workshop 게시글 번호 받아오기
        workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")
        //// 다른 루트에서 오지 않을 경우 받을 수 있는 방법 생각해서 defValue에 넣어두기

        binding.btPlanDetailTimelinePlus.setOnClickListener {
            // Dialog만들기
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog05, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)

            val  mAlertDialog = mBuilder.show()

            SocketApplication.prefs.setString(
                "now_timeline_date",
                binding.tvPlanDetailTimelineDate.text.toString()
            ) // 몇일자 타임라인 불러오는 것인지 파악

            val okButton = mDialogView.findViewById<Button>(R.id.btn_dialog_just_new)
            okButton.setOnClickListener {
                activity?.let{
                    mAlertDialog.dismiss()
                    val intent_new = Intent(activity, Plan_detail_timeline_plus_Activity::class.java)
                    SocketApplication.prefs.setString(
                        "from_to_bookmark",
                        "else"
                    ) // 북마크에서 어디에서 오는지 식별하게 만들어주기 위한 값
                    startActivity(intent_new)
                }
            }

            val noButton = mDialogView.findViewById<Button>(R.id.btn_dialog_bookmark)
            noButton.setOnClickListener{
                activity?.let{
                    mAlertDialog.dismiss()
                    val intent_bookmark = Intent(activity, BookmarkActivity::class.java)
                    SocketApplication.prefs.setString(
                        "from_to_bookmark",
                        "timeline"
                    ) // 북마크에서 어디에서 오는지 식별하게 만들어주기 위한 값
                    startActivity(intent_bookmark)
                }
            }


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
                    Log.d("Aaa", item_result.part.toString())
                    if(item_result.part.toString().equals("참가자")){
                        binding.btPlanDetailTimelinePlus.visibility = View.GONE
                    }
                    else{
                        binding.btPlanDetailTimelinePlus.visibility = View.VISIBLE

                    }
                }
            }


        // 날짜 받는 형식 지정
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")



        // 다음으로 날짜 눌렀을 때
        binding.btPlanDetailTimelineNext.setOnClickListener {
            // 현재 표기되어 있는 날짜 받아오기
            var currentdate =  LocalDate.parse(binding.tvPlanDetailTimelineDate.text, formatter)


            if(currentdate.plusDays(1).isAfter(enddate)){
                binding.tvPlanDetailTimelineDate.setText(startdate.format(formatter))
                firestore_get()
            }
            else{
                currentdate = currentdate.plusDays(1)
                binding.tvPlanDetailTimelineDate.setText(currentdate.format(formatter))
                firestore_get()
            }



        }

        binding.btPlanDetailTimelineBack.setOnClickListener {

            var currentdate =  LocalDate.parse(binding.tvPlanDetailTimelineDate.text, formatter)

            if(currentdate.minusDays(1).isBefore(startdate) ){
                binding.tvPlanDetailTimelineDate.setText(enddate.format(formatter))
                firestore_get()
            }
            else{
                currentdate = currentdate.minusDays(1)
                binding.tvPlanDetailTimelineDate.setText(currentdate.format(formatter))
                firestore_get()
            }

        }



        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        // 날짜 받는 형식 지정
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        // 초반 날짜 설정
        db.collection("workshop")
            .document(workshop_docID)
            .get()
            .addOnSuccessListener {
                    result -> // 성공
                val item = result.toObject(PlanWorkShopData::class.java)
                if (item != null) {
                    binding.tvPlanDetailTimelineDate.setText(item.tv_plan_date_start)
                    startdate = LocalDate.parse(item.tv_plan_date_start.toString(), formatter)
                    enddate = LocalDate.parse(item.tv_plan_date_end.toString(), formatter)

                    // 초반 리스트 설정
                    firestore_get()
                }

            }    }

    fun firestore_get(){

        db.collection("workshop")
            .document(workshop_docID)
            .collection("date")
            .document(binding.tvPlanDetailTimelineDate.text.toString())
            .collection("timeline")
            .orderBy("plan_time_start", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result -> // 성공

                itemList.clear()

                for (document in result) {
                    val item = document.toObject(PlanTimeLineData::class.java)
                    item.docID = document.id// 내부적으로 식별할 수 있는 게시물 식별자
                    itemList.add(item)
                    adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신

                }
                adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신

                if(itemList.isEmpty()){
                    binding.tvPlanDetailTimelineNull.visibility = View.VISIBLE
                }
                else{
                    binding.tvPlanDetailTimelineNull.visibility = View.GONE

                }
            }
            .addOnFailureListener { exception -> // 실패
                Log.d("lee", "Error getting documents: ", exception)
            }

    }
}