package com.example.worktrip.My

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.Home.DetailCourseActivity
import com.example.worktrip.Home.DetailFoodActivity
import com.example.worktrip.Home.DetailLodgingActivity
import com.example.worktrip.Home.DetailProgramActivity
import com.example.worktrip.NetworkThread_detailCommon2
import com.example.worktrip.Plan.Plan_detail_timeline_plus_Activity
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.detail_contentOverview
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private lateinit var recyclerView_bookmark: RecyclerView
val list_card_image_title_overview_location: ArrayList<data_card_image_title_overview_location> = ArrayList()
//val list_card_image_title_overview_location= mutableListOf<data_card_image_title_overview_location>()


var bookmarkImg=""
var bookmarkTitle=""
var bookmarkOverview=""
var bookmarkLocation=""
var bookmarkId=""
var bookmarkTypeId=""

class fragment_bookmark_list : Fragment() {
    lateinit var mAuth: FirebaseAuth
    private var adapter= RecyclerAdapter_card_image_title_overview_location(list_card_image_title_overview_location)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth =FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_bookmark_list, container, false)

        firestore_bookmark_list.collection("user_bookmark")
            .document("${mAuth.currentUser?.uid.toString()}").collection("list").get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    bookmarkImg = document.data["contentImage"].toString() //필드 데이터
                    bookmarkTitle = document.data["contentTitle"].toString() //필드 데이터
                    bookmarkLocation = document.data["contentLocation"].toString() //필드 데이터
                    bookmarkId = document.data["contentID"].toString() //필드 데이터
                    bookmarkTypeId = document.data["contentTypeID"].toString() //필드 데이터
                    bookmarkOverview = document.data["contentOverview"].toString() //필드 데이터


                    list_card_image_title_overview_location.add(
                        data_card_image_title_overview_location(bookmarkImg, bookmarkTitle, bookmarkOverview, bookmarkLocation, bookmarkId, bookmarkTypeId))
                }


                if (list_card_image_title_overview_location.size == 0) {
                    Toast.makeText(context, "북마크에 저장된 정보가 없습니다.", Toast.LENGTH_LONG).show()
                } else {
                    //recycler view
                    recyclerView_bookmark = view.findViewById(R.id.rv_fragment_bookmark_list!!) as RecyclerView
                    recyclerView_bookmark.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    recyclerView_bookmark.adapter = adapter



                    adapter.setOnClickListener(object :
                        RecyclerAdapter_card_image_title_overview_location.ItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            var intent = Intent()

                            var from = SocketApplication.prefs.getString("from_to_bookmark", "else")
                            if (from.equals("timeline")) {
                                intent = Intent(context, Plan_detail_timeline_plus_Activity::class.java)
                                intent.putExtra("title",list_card_image_title_overview_location[position].title)
                                intent.putExtra("place",list_card_image_title_overview_location[position].location)
                                startActivity(intent)

                                // bookmark Activity 종료시키기

                            } else {
                                //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                                if (list_card_image_title_overview_location[position].typeid.equals("&contentTypeId=25")) //여행 코스
                                {
                                    intent = Intent(context, DetailCourseActivity::class.java)
                                } else if (list_card_image_title_overview_location[position].typeid.equals("&contentTypeId=32")) //숙소
                                {
                                    intent = Intent(context, DetailLodgingActivity::class.java)
                                } else if (list_card_image_title_overview_location[position].typeid.equals("&contentTypeId=39")) //맛집
                                {
                                    intent = Intent(context, DetailFoodActivity::class.java)
                                } else if (list_card_image_title_overview_location[position].typeid.equals("program")) //프로그램
                                {
                                    intent = Intent(context, DetailProgramActivity::class.java)
                                }


                                intent.putExtra("contentTypeId", list_card_image_title_overview_location[position].typeid)
                                intent.putExtra("contentId", list_card_image_title_overview_location[position].id)

                                startActivity(intent)
                            }


                        }
                    })
                }
            }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //초기화
        list_card_image_title_overview_location.clear()
    }

}