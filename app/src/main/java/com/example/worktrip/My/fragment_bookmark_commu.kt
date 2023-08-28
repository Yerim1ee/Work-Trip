package com.example.worktrip.My

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.Community.DetailWritingActivity
import com.example.worktrip.Community.RecyclerAdapter_card_community
import com.example.worktrip.Community.data_card_community
import com.example.worktrip.Community.firestore_community
import com.example.worktrip.Community.list_card_community
import com.example.worktrip.Community.recyclerView_community
import com.example.worktrip.R
import com.google.firebase.auth.FirebaseAuth

var bookmarkCommuListImg1=""
var bookmarkCommuListImg2="" //
var bookmarkCommuListImg3="" //
var bookmarkCommuListTitle=""
var bookmarkCommuListContent="" //
var bookmarkCommuListDepature=""
var bookmarkCommuListDestination=""
var bookmarkCommuListDate=""
var bookmarkCommuListCompany=""
var bookmarkCommuListPeople=""
var bookmarkCommuListPeriod=""
var bookmarkCommuListGoal=""
var bookmarkCommuListKeyword=""
var bookmarkCommuListMoney=""
var bookmarkCommuListGood=""
var bookmarkCommuListWritingID=""
var bookmarkCommuListUserID=""


class fragment_bookmark_commu : Fragment() {
    lateinit var mAuth: FirebaseAuth
    private var adapter= RecyclerAdapter_card_community(list_card_community)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookmark_commu, container, false)

        //게시글 불러오기
        firestore_bookmark_list.collection("user_bookmark")
            .document("${mAuth.currentUser?.uid.toString()}").collection("community")
            .get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    bookmarkCommuListImg1 = document.data["img1"].toString() //필드 데이터
                    bookmarkCommuListImg2 = document.data["img2"].toString() //필드 데이터 //
                    bookmarkCommuListImg3= document.data["img3"].toString() //필드 데이터 //
                    bookmarkCommuListTitle = document.data["title"].toString() //필드 데이터
                    bookmarkCommuListContent = document.data["content"].toString() //필드 데이터 //
                    bookmarkCommuListDepature = document.data["depature"].toString() //필드 데이터
                    bookmarkCommuListDestination = document.data["destination"].toString() //필드 데이터
                    bookmarkCommuListDate = document.data["date"].toString() //필드 데이터
                    bookmarkCommuListCompany = document.data["company"].toString() //필드 데이터
                    bookmarkCommuListPeople = document.data["people"].toString() //필드 데이터
                    bookmarkCommuListPeriod = document.data["period"].toString() //필드 데이터
                    bookmarkCommuListGoal = document.data["goal"].toString() //필드 데이터
                    bookmarkCommuListKeyword = document.data["keyword"].toString() //필드 데이터
                    bookmarkCommuListMoney = document.data["money"].toString() //필드 데이터
                    bookmarkCommuListWritingID = document.data["writingID"].toString() //필드 데이터
                    bookmarkCommuListUserID = document.data["userID"].toString() //필드 데이터


                    list_card_community.add(
                        data_card_community(bookmarkCommuListImg1, bookmarkCommuListImg2, bookmarkCommuListImg3, bookmarkCommuListTitle, bookmarkCommuListContent, bookmarkCommuListDepature, bookmarkCommuListDestination,  bookmarkCommuListDate, bookmarkCommuListCompany, bookmarkCommuListPeople, bookmarkCommuListPeriod, bookmarkCommuListGoal, bookmarkCommuListKeyword, bookmarkCommuListMoney, bookmarkCommuListWritingID, bookmarkCommuListUserID)
                    )
                }

                //샘플 데이터 삭제
                //list_card_community.removeFirst()

                //총 게시글 수
                //writingCount.text= list_card_community.size.toString()

                if (list_card_community.size == 0) {
                    Toast.makeText(context, "북마크에 저장된 정보가 없습니다.", Toast.LENGTH_LONG).show()
                } else {
                    //recycler view
                    recyclerView_community =view.findViewById(R.id.rv_fragment_bookmark_commu!!)as RecyclerView
                    recyclerView_community.layoutManager= LinearLayoutManager(requireContext())
                    recyclerView_community.adapter= adapter



                    adapter.setOnClickListener(object :
                        RecyclerAdapter_card_community.ItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            var intent = Intent()

                            intent = Intent(context, DetailWritingActivity::class.java)
                            intent.putExtra("writingID", list_card_community[position].writingid)

                            startActivity(intent)

                        }
                    })
                }
            }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //초기화
        list_card_community.clear()
    }
}