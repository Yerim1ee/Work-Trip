package com.worktrip.Community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.worktrip.databinding.BottomsheetReportBinding

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
            "writingID" to com.worktrip.Community.dbWritingID,
            "date" to com.worktrip.Community.dbWritingDate,
            "company" to com.worktrip.Community.dbWritingCompany,
            "depature" to com.worktrip.Community.dbWritingDepature,
            "destination" to com.worktrip.Community.dbWritingDestination,
            "people" to com.worktrip.Community.dbWritingPeople,
            "period" to com.worktrip.Community.dbWritingPeriod,
            "goal" to com.worktrip.Community.dbWritingGoal,
            "keyword" to com.worktrip.Community.dbWritingKeyword,
            "money" to com.worktrip.Community.dbWritingMoney,
            "img1" to com.worktrip.Community.dbWritingImg1,
            "img2" to com.worktrip.Community.dbWritingImg2,
            "img3" to com.worktrip.Community.dbWritingImg3,
            "title" to com.worktrip.Community.dbWritingTitle,
            "content" to com.worktrip.Community.dbWritingContent,
            "userID" to com.worktrip.Community.dbWritingUserID
        )
        //게시글 신고
        binding.btnBottomsheetReport.setOnClickListener {

        var reportReason=etReportReason.text.toString()

            val data_community_report_reason = hashMapOf(
                "userID" to "${mAuth.currentUser?.uid.toString()}",
                "reason" to reportReason
            )

            val data_block_list = hashMapOf(
                "userID" to com.worktrip.Community.dbWritingUserID
            )

            //신고 리스트에 추가
            com.worktrip.Community.firestore_community_report.collection("community_report")
                .document(com.worktrip.Community.dbWritingID).set(data_community_report_info)

            com.worktrip.Community.firestore_community_report.collection("community_report")
                .document(com.worktrip.Community.dbWritingID).collection("reason").document("${mAuth.currentUser?.uid.toString()}").set(data_community_report_reason)

            Toast.makeText(context, "신고 및 차단 요청이 접수되었습니다. 해당 게시글은 관리자의 확인 후 조치가 취해집니다.", Toast.LENGTH_LONG).show()

            //해당 게시글 목록에서 삭제
            com.worktrip.Community.firestore_community.collection("community").document(com.worktrip.Community.dbWritingID).delete()

            //작성자 차단
            com.worktrip.Community.firestore_community_report.collection("user")
                .document("${mAuth.currentUser?.uid.toString()}").collection("block_list").document(
                    com.worktrip.Community.dbWritingUserID
                ).set(data_block_list)

            onDestroyView()

        }

    }
}