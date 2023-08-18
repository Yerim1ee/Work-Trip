package com.example.worktrip.My

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.worktrip.Home.DetailCourseActivity
import com.example.worktrip.Home.DetailFoodActivity
import com.example.worktrip.Home.DetailLodgingActivity
import com.example.worktrip.Home.DetailProgramActivity
import com.example.worktrip.Home.RecyclerAdapter_card_list
import com.example.worktrip.Home.firestore_bookmark_list
import com.example.worktrip.NetworkThread_detailCommon2
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityBookmarkBinding
import com.example.worktrip.detail_contentOverview
import com.example.worktrip.list_card_list
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private lateinit var binding : ActivityBookmarkBinding

private lateinit var viewPager_bookmark: ViewPager2
private lateinit var tabLayout_bookmark: TabLayout

private lateinit var recyclerView_bookmark: RecyclerView
val list_card_image_title_overview_location: ArrayList<data_card_image_title_overview_location> = ArrayList()

var bookmarkImg=""
var bookmarkTitle=""
var bookmarkOverview=""
var bookmarkLocation=""
var bookmarkId=""
var bookmarkTypeId=""

class BookmarkActivity  : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private var adapter= RecyclerAdapter_card_image_title_overview_location(list_card_image_title_overview_location)
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth =FirebaseAuth.getInstance()
        updateOverview()

        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_bookmark)

        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_bookmark))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 (manifast에서 연결 필요)
        val toolbarTitle = view.findViewById<TextView>(R.id.tv_activity_bookmark_title)
        toolbarTitle.text="저장한 게시글"

        //탭레이아웃 설정
        viewPager_bookmark = findViewById<ViewPager2>(R.id.vp_activity_bookmark)
        tabLayout_bookmark = findViewById<TabLayout>(R.id.tl_activity_bookmark)

        val viewPagerAdapter = TabAdapter_bookmark(this)

        // fragment add
        viewPagerAdapter.addFragment(fragment_bookmark_list())
        viewPagerAdapter.addFragment(fragment_bookmark_commu())

        // adapter 연결
        viewPager_bookmark?.adapter = viewPagerAdapter
        viewPager_bookmark?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")
            }
        })
        // 뷰페이저와 탭레이아웃을 붙임
        TabLayoutMediator(tabLayout_bookmark, viewPager_bookmark){ tab, position ->
            when (position) {
                0 -> tab.text = "추천 정보"
                1 -> tab.text = "커뮤니티"
                else -> tab.text = "Tab ${position + 1}"
            }
        }.attach()
        //----------------------------------------------------

        getFirestoreData()

    }

    override fun onDestroy() {
        super.onDestroy()
        //초기화
        list_card_image_title_overview_location.clear()
    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search, menu)
        return true
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            android.R.id.home -> {
                finish()
                return super.onOptionsItemSelected(item)
            }

            R.id.it_toolbar_s_search -> {
                //검색 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                //intent = Intent(this, HomeSearchActivity::class.java)
                //startActivity(intent)
                return super.onOptionsItemSelected(item)

            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

fun updateOverview(){
    firestore_bookmark_list.collection("user_bookmark")
        .document("${mAuth.currentUser?.uid.toString()}").collection("list").get()
        .addOnSuccessListener { task ->
            for (document in task) {
                bookmarkId = document.data["contentID"].toString() //필드 데이터
                bookmarkTypeId = document.data["contentTypeID"].toString() //필드 데이터

                if (document.data["contentOverview"].toString().equals("")) {
                    //overview 추가로 불러오기
                    val url_detailCommon2 =
                        "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D" + "&MobileOS=AND" + "&MobileApp=WorkTrip" + "&_type=xml&contentId=" + bookmarkId + bookmarkTypeId + "&defaultYN=N" + "&firstImageYN=N" + "&areacodeYN=N" + "&catcodeYN=N" + "&addrinfoYN=N" + "&mapinfoYN=N" + "&overviewYN=Y" + "&pageNo=1"
                    val thread_detailCommon2 =
                        Thread(NetworkThread_detailCommon2(url_detailCommon2))
                    thread_detailCommon2.start() // 쓰레드 시작
                    thread_detailCommon2.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                    firestore_bookmark_list.collection("user_bookmark")
                        .document("${mAuth.currentUser?.uid.toString()}").collection("list")
                        .document(bookmarkId).update("contentOverview", detail_contentOverview)

                    detail_contentOverview=document.data["contentOverview"].toString()
                }
                else
                {
                    bookmarkId=""
                    bookmarkTypeId=""
                }
            }
        }
}
    fun getFirestoreData() {
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
                        data_card_image_title_overview_location(
                            bookmarkImg,
                            bookmarkTitle,
                            bookmarkOverview,
                            bookmarkLocation,
                            bookmarkId,
                            bookmarkTypeId
                        )
                    )
                }


                if (list_card_image_title_overview_location.size == 0) {
                    Toast.makeText(applicationContext, "북마크에 저장된 정보가 없습니다.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    //recycler view
                    recyclerView_bookmark =
                        findViewById(R.id.rv_activity_bookmark_list!!) as RecyclerView
                    recyclerView_bookmark.layoutManager =
                        LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false)
                    recyclerView_bookmark.adapter = adapter

                    var intent = intent

                    adapter.setOnClickListener(object :
                        RecyclerAdapter_card_image_title_overview_location.ItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                            if (list_card_image_title_overview_location[position].typeid.equals("&contentTypeId=25")) //여행 코스
                            {
                                intent = Intent(this@BookmarkActivity, DetailCourseActivity::class.java)
                            } else if (list_card_image_title_overview_location[position].typeid.equals("&contentTypeId=32")) //숙소
                            {
                                intent = Intent(this@BookmarkActivity, DetailLodgingActivity::class.java)
                            } else if (list_card_image_title_overview_location[position].typeid.equals("&contentTypeId=39")) //맛집
                            {
                                intent = Intent(this@BookmarkActivity, DetailFoodActivity::class.java)
                            } else if (list_card_image_title_overview_location[position].typeid.equals("program")) //프로그램
                            {
                                intent = Intent(this@BookmarkActivity, DetailProgramActivity::class.java)
                            }


                            intent.putExtra("contentTypeId", list_card_image_title_overview_location[position].typeid)
                            intent.putExtra("contentId", list_card_image_title_overview_location[position].id)

                            startActivity(intent)
                        }
                    })
                }
            }
    }

}

