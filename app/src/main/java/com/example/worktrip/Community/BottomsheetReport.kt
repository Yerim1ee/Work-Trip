package com.example.worktrip.Community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.R
import com.example.worktrip.databinding.BottomsheetReportBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val firestore_community_report= Firebase.firestore

class BottomsheetReport  : BottomSheetDialogFragment() {
    lateinit var binding: BottomsheetReportBinding
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding=BottomsheetReportBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.bottomsheet_report, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        var etReportReason= binding.etBottomsheetReport
        val data_community_report_info = hashMapOf(
            "writingID" to dbWritingID,
            "date" to dbWritingDate,
            "company" to dbWritingCompany,
            "depature" to dbWritingDepature,
            "destination" to dbWritingDestination,
            "people" to dbWritingPeople,
            "period" to dbWritingPeriod,
            "goal" to dbWritingGoal,
            "keyword" to dbWritingKeyword,
            "money" to dbWritingMoney,
            "img1" to dbWritingImg1,
            "img2" to dbWritingImg2,
            "img3" to dbWritingImg3,
            "title" to dbWritingTitle,
            "content" to dbWritingContent,
            "userID" to dbWritingUserID
        )
        //게시글 신고
        binding.btnBottomsheetReport.setOnClickListener {

        var reportReason=etReportReason.text.toString()

            val data_community_report_reason = hashMapOf(
                "userID" to "${mAuth.currentUser?.uid.toString()}",
                "reason" to reportReason
            )

            val data_block_list = hashMapOf(
                "userID" to dbWritingUserID
            )

            //신고 리스트에 추가
            firestore_community_report.collection("community_report")
                .document(dbWritingID).set(data_community_report_info)

            firestore_community_report.collection("community_report")
                .document(dbWritingID).collection("reason").document("${mAuth.currentUser?.uid.toString()}").set(data_community_report_reason)

            Toast.makeText(context, "신고 및 차단 요청이 접수되었습니다. 해당 게시글은 관리자의 확인 후 조치가 취해집니다.", Toast.LENGTH_LONG).show()

            //해당 게시글 목록에서 삭제
            firestore_community.collection("community").document(dbWritingID).delete()

            //작성자 차단
            firestore_community_report.collection("user")
                .document("${mAuth.currentUser?.uid.toString()}").collection("block_list").document(
                    dbWritingUserID).set(data_block_list)

            onDestroyView()

        }

    }
}