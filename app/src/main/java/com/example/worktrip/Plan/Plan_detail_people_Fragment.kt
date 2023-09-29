package com.example.worktrip.Plan

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.MainActivity
import com.example.worktrip.Plan.Adapter.Plan_detail_person_Adapter
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.FragmentPlanDetailPeopleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate


class Plan_detail_people_Fragment : Fragment() {

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    // 1. Context를 할당할 변수를 프로퍼티로 선언(어디서든 사용할 수 있게)
    lateinit var mainActivity: Plan_workshop_details_Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as Plan_workshop_details_Activity
    }

    private lateinit var binding: FragmentPlanDetailPeopleBinding
    lateinit var workshop_docID2: String

    val itemList = mutableListOf<PlanWorkShopUserData>()
    val adapter = Plan_detail_person_Adapter(MainActivity(),itemList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanDetailPeopleBinding.inflate(inflater, container, false)
        // 리사이클러뷰 연결
        binding.rcvPlanDetailPeopleRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.rcvPlanDetailPeopleRecyclerview.adapter= adapter

        auth = Firebase.auth // auth 정의

        // workshop 게시글 번호 받아오기
        workshop_docID2 = SocketApplication.prefs.getString("now_workshop_id", "")
        //// 다른 루트에서 오지 않을 경우 받을 수 있는 방법 생각해서 defValue에 넣어두기


        binding.tvPlanPeopleCode.setText(workshop_docID2)


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
                    }
                    else if (sharingResult != null) {
                        startActivity(sharingResult.intent)
                    }
                }
            }else {
                Toast.makeText(activity, "카카오톡이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show()

            }
        }

        return binding.root
    }

    override fun onResume(){
        super.onResume()

        firestore_get_manager()
        firestore_get_participant()
    }

    fun firestore_get_title(): String{

        var title:String = ""
        db.collection("workshop")
            .document(workshop_docID2)
            .get()
            .addOnSuccessListener { result -> // 성공
                val item = result.toObject(PlanWorkShopData::class.java)
                if (item != null) {
                   title =  item.tv_plan_title.toString()
                }
            }
            .addOnFailureListener {
            }
        return title
    }
    fun firestore_get_participant(){
        itemList.clear()

        db.collection("user_workshop")
            .get()
            .addOnSuccessListener {
                    result_first ->
                for(document_first in result_first){
                    db.collection("user_workshop")
                        .document(document_first.id)
                        .collection("workshop_list")
                        .document(workshop_docID2)
                        .get()
                        .addOnSuccessListener {result ->
                            val item = result.toObject(PlanWorkShopUserData::class.java)
                            if (item != null) {
                                item.workshop_docID = result.id
                                if(item.part.equals("참가자")){
                                    itemList.add(item)
                                }
                            }
                            // 리사이클러 뷰 갱신
                            adapter.notifyDataSetChanged()
                        }
                }


            }
    }

    fun firestore_get_manager(){

        db.collection("user_workshop")
            .get()
            .addOnSuccessListener {
                    result_first ->
                for(document_first in result_first){
                    db.collection("user_workshop")
                        .document(document_first.id)
                        .collection("workshop_list")
                        .document(workshop_docID2)
                        .get()
                        .addOnSuccessListener {result ->
                            val item = result.toObject(PlanWorkShopUserData::class.java)
                            if (item != null) {
                                item.workshop_docID = result.id
                                if(item.part.equals("기획자")){
                                    itemList.add(item)
                                }
                            }
                            // 리사이클러 뷰 갱신
                            adapter.notifyDataSetChanged()
                        }
                }


            }



    }

}
