package com.example.worktrip.Home

import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.NetworkThread_list
import com.example.worktrip.NoticeActivity
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.list_card_list
import com.example.worktrip.list_contentId


private val key="599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
//한 페이지 결과 수
private val numOfRows ="&numOfRows=50"
//페이지번호
//private val pageNo="&pageNo=1"
//AND(안드로이드)
private val mobileOS = "&MobileOS=AND"
//서비스명 = 어플명
private val mobileApp = "&MobileApp=WorkTrip"
//type (xml/json)
private val _type="&_type=xml"
//목록구분(Y=목록, N=개수)
private val listYN="&listYN=Y"
//정렬구분 (A=제목순, C=수정일순, D=생성일순) 대표 이미지가 반드시 있는 정렬(O=제목순, Q=수정일순, R=생성일순)
private val arrange="&arrange=O"
//관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
private var contentTypeId=""

class fragment_home : Fragment() {
    //recyclerView
    val list_course: ArrayList<data_card_list> = ArrayList()
    private var adapter_course=RecyclerAdapter_card_image_title(list_course)


    val list2: ArrayList<data_list_image_title_overview> = ArrayList()

    lateinit var recyclerView_course: RecyclerView
    lateinit var recyclerView_keyword: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list2.add(data_list_image_title_overview("null", "11111", "11111111111111111111111111111111111111111111111"))
        list2.add(data_list_image_title_overview("null", "22222", "22222222222222222222222222222222222222222222222"))
        list2.add(data_list_image_title_overview("null", "33333", "33333333333333333333333333333333333333333333333"))

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //toolbar
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.tb_fragment_home))
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        setHasOptionsMenu(true)

        //user-name
        var userName=view.findViewById<TextView>(R.id.tv_fragment_home_userName)
        userName.setText(SocketApplication.prefs.getString("user-name","익명")+" 님,")

        /*//링크 테스트용...
        userName.setOnClickListener{
            val uri = Uri.parse("worktrip://detailcourse?contentID=2361026") //scheme://host
            val i = Intent(Intent.ACTION_VIEW, uri)
            i.setPackage("com.example.worktrip")
        }*/

        //API 정보를 가지고 있는 주소
        url_list = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + key + numOfRows + mobileOS + mobileApp + _type + listYN + arrange + "&contentTypeId=25"
        //println(url_list)

        //쓰레드 생성
        val thread = Thread(NetworkThread_list(url_list))
        thread.start() // 쓰레드 시작
        thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력

        //추천 코스
        for (i in 0..2)
        {
            val randomCourse=list_card_list.random()
            list_course.add(randomCourse)
        }
        recyclerView_course=view.findViewById(R.id.rv_fragment_home_recommendedCourse!!)as RecyclerView
        recyclerView_course.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        //recyclerView_course.adapter= RecyclerAdapter_card_image_title(list_course)
        recyclerView_course.adapter=adapter_course

        adapter_course.setOnClickListener( object : RecyclerAdapter_card_image_title.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                var intent_course = Intent(context, DetailCourseActivity::class.java)

                intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                intent_course.putExtra("contentId", list_course[position].id)

                startActivity(intent_course)
                list_contentId =""
                contentTypeId=""
            }
        })

        //키워드 추천
        recyclerView_keyword=view.findViewById(R.id.rv_fragment_home_recommendedKeyword!!)as RecyclerView
        recyclerView_keyword.layoutManager=LinearLayoutManager(requireContext())
        recyclerView_keyword.adapter= RecyclerAdapter_list_image_title_overview(list2)

        //category intent
        val categoryCourse = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_course)
        val categoryLodging = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_lodging)
        val categoryFood = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_food)
        val categoryProgram = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_program)

        //var contentTypeId=""
        //var contentCat1=""
        //var contentCat2=""


        categoryCourse.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="&contentTypeId=25" //여행 코스
                //contentCat1="&cat1=C01" //분류 코드 1

                val intent = Intent(context, ListRecommendedActivity::class.java)
                intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)

                startActivity(intent)

                contentTypeId=""
                //contentCat1=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

        categoryLodging.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="&contentTypeId=32" //숙소
                //contentCat1="&cat1=B02" //분류 코드 1
                //contentCat2="&cat2=B0201" //분류 코드 2

                val intent = Intent(context, ListRecommendedActivity::class.java)
                intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)
                //intent.putExtra("contentCat2", contentCat2)

                startActivity(intent)

                contentTypeId=""
                //contentCat1=""
                //contentCat2=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

        categoryProgram.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="program" //프로그램
                val intent = Intent(context, ListProgramActivity::class.java)
                //intent.putExtra("contentTypeId", contentTypeId)
                startActivity(intent)
                contentTypeId=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

        categoryFood.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="&contentTypeId=39" //맛집
                //contentCat1="&cat1=A05" //분류 코드 1
                //contentCat2="&cat1=A0502" //분류 코드 2

                val intent = Intent(context, ListRecommendedActivity::class.java)
                intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)
                //intent.putExtra("contentCat2", contentCat2)

                startActivity(intent)

                contentTypeId=""
                //contentCat1=""
                //contentCat2=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

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
                val intent = Intent(context, HomeSearchActivity::class.java)
                intent.putExtra("searchCheck", "course")

                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }
}