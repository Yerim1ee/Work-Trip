package com.example.worktrip.Plan

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.StateSet.TAG
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.PlanWorkShopPeopleData
import com.example.worktrip.MainActivity
import com.example.worktrip.Plan.Adapter.Plan_detail_budget_Adapter
import com.example.worktrip.Plan.Adapter.Plan_detail_person_Adapter
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.FragmentPlanDetailPeopleBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate


class Plan_detail_people_Fragment : Fragment() {

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    // 1. Context를 할당할 변수를 프로퍼티로 선언(어디서든 사용할 수 있게)
    lateinit var mainActivity: Plan_workshop_details_Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as Plan_workshop_details_Activity
    }

    private lateinit var binding: FragmentPlanDetailPeopleBinding
    lateinit var workshop_docID: String

    val itemList = mutableListOf<PlanWorkShopPeopleData>()
    val adapter = Plan_detail_person_Adapter(MainActivity(),itemList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanDetailPeopleBinding.inflate(inflater, container, false)

        // 리사이클러뷰 연결
        binding.rcvPlanDetailPeopleRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.rcvPlanDetailPeopleRecyclerview.adapter= adapter


        // workshop 게시글 번호 받아오기
        workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")
        //// 다른 루트에서 오지 않을 경우 받을 수 있는 방법 생각해서 defValue에 넣어두기

        binding.tvPlanPeopleCode.setText(workshop_docID)


        // 텍스트를 클립보드에 복사하기
        binding.ibPlanPeopleCopy.setOnClickListener {
            val getText = binding.tvPlanPeopleCode.text.toString()

            val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("workshop_goin_code", getText)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(activity, "텍스트가 클립보드에 복사되었습니다.",Toast.LENGTH_SHORT).show()
        }
        binding.ibPlanPeopleShare.setOnClickListener {


            val defaultText = TextTemplate(
                text = """
                    
        ${ firestore_get_title() + "의 초대 코드입니다" + "\n" 
                        + binding.tvPlanPeopleCode.text.toString()}
    """.trimIndent(),
                link = Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )

            )
            // 카카오톡 설치여부 확인
            if (ShareClient.instance.isKakaoTalkSharingAvailable(mainActivity)) {
                // 카카오톡으로 카카오톡 공유 가능
                ShareClient.instance.shareDefault(mainActivity, defaultText) { sharingResult, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡 공유 실패", error)
                    }
                    else if (sharingResult != null) {
                        Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                        startActivity(sharingResult.intent)

                        // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                        Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                    }
                }
            }else {
                Toast.makeText(activity, "카카오톡이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show()

            }
        }

        return binding.root
    }


    fun firestore_get_title(): String{

         var title:String = ""
        db.collection("workshop")
            .document(workshop_docID)
            .get()
            .addOnSuccessListener { result -> // 성공
                val item = result.toObject(PlanWorkShopData::class.java)
                if (item != null) {
                   title =  item.tv_plan_title.toString()
                }
            }
            .addOnFailureListener { exception -> // 실패
                Log.d("lee", "Error getting documents: ", exception)
            }
        return title
    }
    fun firestore_get(){


    }
}