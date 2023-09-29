package com.example.worktrip.Community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.Home.DetailCourseActivity
import com.example.worktrip.Home.DetailFoodActivity
import com.example.worktrip.Home.DetailLodgingActivity
import com.example.worktrip.Home.DetailProgramActivity
import com.example.worktrip.Home.HomeSearchActivity
import com.example.worktrip.Home.ListRecommendedActivity
import com.example.worktrip.Home.RecyclerAdapter_card_list
import com.example.worktrip.Home.RecyclerAdapter_list_image_title_overview
import com.example.worktrip.My.RecyclerAdapter_card_image_title_overview_location
import com.example.worktrip.My.bookmarkId
import com.example.worktrip.My.bookmarkImg
import com.example.worktrip.My.bookmarkLocation
import com.example.worktrip.My.bookmarkOverview
import com.example.worktrip.My.bookmarkTitle
import com.example.worktrip.My.bookmarkTypeId
import com.example.worktrip.My.data_card_image_title_overview_location
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.My.list_card_image_title_overview_location
import com.example.worktrip.NoticeActivity
import com.example.worktrip.Plan.Plan_detail_timeline_plus_Activity
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.list_card_list
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_community.card_community


val firestore_community= Firebase.firestore

val list_card_community: ArrayList<data_card_community> = ArrayList()
lateinit var recyclerView_community: RecyclerView

var commuListImg1=""
var commuListImg2=""
var commuListImg3=""
var commuListTitle=""
var commuListContent=""
var commuListDepature=""
var commuListDestination=""
var commuListDate=""
var commuListCompany=""
var commuListPeople=""
var commuListPeriod=""
var commuListGoal=""
var commuListKeyword=""
var commuListMoney=""
//var commuListGood=0
var commuListWritingID=""
var commuListUserID=""

class fragment_community : Fragment() {
    private var adapter= RecyclerAdapter_card_community(list_card_community)
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_community, container, false)
        mAuth = FirebaseAuth.getInstance()

        //toolbar
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.tb_fragment_community))
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        setHasOptionsMenu(true)

        val plusWriting = view.findViewById<FloatingActionButton>(R.id.fb_fragment_community_write)
        val writingCount = view.findViewById<TextView>(R.id.tv_fragment_community_count)
        var blocked_users: MutableList<String> = mutableListOf()

        //차단 리스트 검사
        firestore_community.collection("user").document("${mAuth.currentUser?.uid.toString()}").collection("block_list").get()
            .addOnSuccessListener { task2 ->
                for(document in task2) {
                    var blocked_user=document.data["userID"].toString()
                    blocked_users.add(blocked_user)
                }
                //게시글 불러오기
                firestore_community.collection("community")
                    .get()
                    .addOnSuccessListener { task ->
                        for (document in task) {
                            commuListWritingID = document.data["writingID"].toString() //필드 데이터
                            commuListUserID = document.data["userID"].toString() //필드 데이터

                            if (!(commuListWritingID.equals("sample")))
                            {
                                if (!blocked_users.contains(commuListUserID))
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
                                    data_card_community(commuListImg1, commuListImg2, commuListImg3, commuListTitle, commuListContent, commuListDepature, commuListDestination, commuListDate, commuListCompany, commuListPeople, commuListPeriod, commuListGoal, commuListKeyword, commuListMoney, commuListWritingID, commuListUserID))
                                }
                            }

                        }

                        //샘플 데이터 삭제
                        //list_card_community.removeFirst()

                        //총 게시글 수
                        writingCount.text= list_card_community.size.toString()

                        if (list_card_community.size == 0) {
                            Toast.makeText(context, "커뮤니티에 작성된 글이 없습니다.", Toast.LENGTH_LONG).show()
                        } else {
                            //recycler view
                            recyclerView_community=view.findViewById(R.id.rv_fragment_community!!)as RecyclerView
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
            }


        plusWriting.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, CommuPlusActivity::class.java)
                //intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)

                startActivity(intent)
            }})
        return view
    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_notice_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            /*R.id.it_toolbar_ns_notice -> {
                //알림 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "알림 실행", Toast.LENGTH_LONG).show()
                val intent = Intent(context, NoticeActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }*/

            R.id.it_toolbar_ns_search -> {
                //검색 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                val intent = Intent(context, CommuSearchActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list_card_community.clear()
    }
}