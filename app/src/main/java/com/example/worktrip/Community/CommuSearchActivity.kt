package com.example.worktrip.Community

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityCommuSearchBinding

private lateinit var binding: ActivityCommuSearchBinding

class CommuSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.worktrip.Community.binding = ActivityCommuSearchBinding.inflate(layoutInflater)
        val view = com.example.worktrip.Community.binding.root
        setContentView(view)
        var adapter= RecyclerAdapter_card_community(list_card_community)
        var intent = Intent(this, DetailWritingActivity::class.java)

        binding.btnActivityCommuSearchOk.setOnClickListener {
            var commuSearchText=binding.etActivityCommuSearch.text.toString()

            list_card_community.clear()

            //게시글 불러오기
            firestore_community.collection("community")
                .get()
                .addOnSuccessListener { task ->
                    for (document in task) {
                        if (document.data["title"].toString().contains(commuSearchText))
                        {
                            commuListImg1 = document.data["img1"].toString() //필드 데이터
                            commuListImg2 = document.data["img2"].toString() //필드 데이터
                            commuListImg3 = document.data["img3"].toString() //필드 데이터
                            commuListTitle = document.data["title"].toString() //필드 데이터
                            commuListContent = document.data["content"].toString() //필드 데이터
                            commuListDepature = document.data["depature"].toString() //필드 데이터
                            commuListDestination = document.data["destination"].toString() //필드 데이터
                            commuListDate = document.data["date"].toString() //필드 데이터
                            commuListCompany = document.data["company"].toString() //필드 데이터
                            commuListPeople = document.data["people"].toString() //필드 데이터
                            commuListPeriod = document.data["period"].toString() //필드 데이터
                            commuListGoal = document.data["goal"].toString() //필드 데이터
                            commuListKeyword = document.data["keyword"].toString() //필드 데이터
                            commuListMoney = document.data["money"].toString() //필드 데이터
                            //commuListGood = document.data["good"].toString() //필드 데이터
                            commuListWritingID = document.data["writingID"].toString() //필드 데이터
                            commuListUserID = document.data["userID"].toString() //필드 데이터


                            list_card_community.add(
                                data_card_community(commuListImg1, commuListImg2, commuListImg3, commuListTitle, commuListContent, commuListDepature, commuListDestination, commuListDate, commuListCompany, commuListPeople, commuListPeriod, commuListGoal, commuListKeyword, commuListMoney, commuListWritingID, commuListUserID)
                            )
                        }
                    }

                    //샘플 데이터 삭제
                    //list_card_community.removeFirst()

                    //총 게시글 수
                    //writingCount.text= list_card_community.size.toString()

                    if (list_card_community.size == 0) {
                        Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        //recycler view
                        recyclerView_community=view.findViewById(R.id.rv_activity_commu_search!!)as RecyclerView
                        recyclerView_community.layoutManager= LinearLayoutManager(this)
                        recyclerView_community.adapter= adapter


                        adapter.setOnClickListener(object :
                            RecyclerAdapter_card_community.ItemClickListener {
                            override fun onClick(view: View, position: Int) {

                                intent.putExtra("writingID", list_card_community[position].writingid)

                                startActivity(intent)

                            }
                        })
                    }
                }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //초기화
        list_card_community.clear()
    }
}