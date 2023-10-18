package com.worktrip.Home

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worktrip.NetworkThread_list
import com.worktrip.R
import com.worktrip.databinding.ActivityListRecommendedBinding
import com.worktrip.list_card_list
import com.worktrip.list_contentId
import com.google.android.material.chip.Chip

private lateinit var binding : ActivityListRecommendedBinding
private lateinit var recyclerView_list: RecyclerView

private lateinit var categoryArray: List<String>

private lateinit var getContentTypeId: String

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
private lateinit var contentTypeId: String
private var areaCode="&areaCode=1"

var url_list=""

private var contentCat1=""
private var contentCat2=""
private var contentCat3=""

private var searchCheck=""
class ListRecommendedActivity  : AppCompatActivity() {
    private var adapter=RecyclerAdapter_card_list(list_card_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRecommendedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_list_recommended)

        Toast.makeText(applicationContext, "목록을 불러오는 중입니다.", Toast.LENGTH_SHORT).show()

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_list_recommended))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = view.findViewById<TextView>(R.id.tv_activity_list_recommended_title)

        //intent
        getContentTypeId = intent.getStringExtra("contentTypeId").toString()

        if (getContentTypeId.equals("&contentTypeId=25")) //여행 코스
        {
            toolbarTitle.text="코스"
            searchCheck="course"
            //칩 추가
            categoryArray= listOf("힐링 코스", "도보 코스", "캠핑 코스", "맛 코스", "가족 코스")
            CategoryChips(categoryArray)
            //
            intent = Intent(this, DetailCourseActivity::class.java)

        }
        else if (getContentTypeId.equals("&contentTypeId=32")) //숙소
        {
            toolbarTitle.text="숙소"
            searchCheck="lodging"
            //칩 추가
            categoryArray= listOf("관광호텔", "콘도미니엄", "유스호스텔", "펜션", "모텔", "민박", "게스트하우스", "서비스드레지던스", "한옥")
            CategoryChips(categoryArray)
            //
            intent = Intent(this, DetailLodgingActivity::class.java)

        }
        else if (getContentTypeId.equals("&contentTypeId=39")) //맛집
        {
            toolbarTitle.text="맛집"
            searchCheck="food"
            //칩 추가
            categoryArray= listOf("한식", "서양식", "일식", "중식", "이색음식점", "카페/전통찻집")
            CategoryChips(categoryArray)
            //
            intent = Intent(this, DetailFoodActivity::class.java)

        }

        //키 값
        //key = "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
        //한 페이지 결과 수
        //numOfRows ="&numOfRows=50"
        //페이지번호
        //val pageNo="&pageNo=1"
        //AND(안드로이드)
        //mobileOS = "&MobileOS=AND"
        //서비스명 = 어플명
        //mobileApp = "&MobileApp=WorkTrip"
        //type (xml/json)
        //_type="&_type=xml"
        //목록구분(Y=목록, N=개수)
        //listYN="&listYN=Y"
        //정렬구분 (A=제목순, C=수정일순, D=생성일순) 대표 이미지가 반드시 있는 정렬(O=제목순, Q=수정일순, R=생성일순)
        //arrange="&arrange=O"
        //관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
        contentTypeId=getContentTypeId

        /*if(getContentTypeId.equals("&contentTypeId=32")) //숙소
        {
            //API 정보를 가지고 있는 주소
            url_list="https://apis.data.go.kr/B551011/KorService1/searchStay1?serviceKey=" + key + mobileOS + mobileApp + _type

            //쓰레드 생성
            val thread = Thread(NetworkThread_list(url_list))
            thread.start() // 쓰레드 시작
            thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력
        }
        else //나머지
        {
            //API 정보를 가지고 있는 주소
            url_list = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + key + numOfRows + mobileOS + mobileApp + _type + listYN + arrange + contentTypeId + areaCode

            //쓰레드 생성
            val thread = Thread(NetworkThread_list(url_list))
            thread.start() // 쓰레드 시작
            thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력
        }*/

        //spinner
        val spinner_location = findViewById<Spinner>(R.id.sp_activity_home_search_location)
        val locationItems = resources.getStringArray(R.array.locationItems)
        val SpinnerAdapter_location =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationItems)

        spinner_location.adapter = SpinnerAdapter_location
        spinner_location.setSelection(0)
        spinner_location.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2)
                {
                    0->{ //서울
                        //Toast.makeText(applicationContext, "0", Toast.LENGTH_LONG).show()
                        areaCode="&areaCode=1"
                        list_card_list.clear()
                    }
                    1->{ //인천
                        areaCode="&areaCode=2"
                        list_card_list.clear()
                    }
                    2->{ //대전
                        areaCode="&areaCode=3"
                        list_card_list.clear()
                    }
                    3->{ //대구
                        areaCode="&areaCode=4"
                        list_card_list.clear()
                    }
                    4->{ //광주
                        areaCode="&areaCode=5"
                        list_card_list.clear()
                    }
                    5->{ //부산
                        areaCode="&areaCode=6"
                        list_card_list.clear()
                    }
                    6->{ //울산
                        areaCode="&areaCode=7"
                        list_card_list.clear()
                    }
                    7->{ //세종특별자치시
                        areaCode="&areaCode=8"
                        list_card_list.clear()
                    }
                    8->{ //경기도
                        areaCode="&areaCode=31"
                        list_card_list.clear()
                    }
                    9->{ //강원특별자치도
                        areaCode="&areaCode=32"
                        list_card_list.clear()
                    }
                    10->{ //충청북도
                        areaCode="&areaCode=33"
                        list_card_list.clear()
                    }
                    11->{ //충청남도
                        areaCode="&areaCode=34"
                        list_card_list.clear()
                    }
                    12->{ //경상북도
                        areaCode="&areaCode=35"
                        list_card_list.clear()
                    }
                    13->{ //경상남도
                        areaCode="&areaCode=36"
                        list_card_list.clear()
                    }
                    14->{ //전라북도
                        areaCode="&areaCode=37"
                        list_card_list.clear()
                    }
                    15->{ //전라남도
                        areaCode="&areaCode=38"
                        list_card_list.clear()
                    }
                    16->{ //제주도
                        areaCode="&areaCode=39"
                        list_card_list.clear()
                    }
                }

                //API 정보를 가지고 있는 주소
                url_list = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + key + numOfRows + mobileOS + mobileApp + _type + listYN + arrange + contentTypeId + areaCode

                //쓰레드 생성
                val thread = Thread(NetworkThread_list(url_list))
                thread.start() // 쓰레드 시작
                thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                //recycler view
                recyclerView_list=findViewById(R.id.rv_activity_list_recommended_list!!)as RecyclerView
                recyclerView_list.layoutManager= GridLayoutManager(parent, 2)
                //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
                recyclerView_list.adapter=adapter

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinner_location.setSelection(0)
            }
        }


        adapter.setOnClickListener( object : RecyclerAdapter_card_list.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                intent.putExtra("contentTypeId", contentTypeId)
                intent.putExtra("contentId", list_card_list[position].id)

                startActivity(intent)
                list_contentId=""
                contentTypeId=""
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        //초기화
        list_card_list.clear()
        areaCode="&areaCode=1"
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

            R.id.it_toolbar_s_search -> {
                //검색 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                intent = Intent(this, HomeSearchActivity::class.java)
                intent.putExtra("searchCheck", searchCheck)

                list_card_list.clear()

                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            android.R.id.home -> {
                finish()
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    //칩 추가
    fun CategoryChips(list:List<String>)
    {
        categoryArray= list
        for (i in 0..list.size-1)
        {
            val chip = Chip(this).apply {
                id= categoryArray.indexOf(categoryArray[i]) //0..list.size-1

                text = categoryArray[i]
                isCheckable = true
                isCheckedIconVisible = false
                chipStrokeWidth = 4f
                setTextSize(TypedValue.COMPLEX_UNIT_DIP , 16f)

                //테두리
                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                    intArrayOf(Color.rgb(39, 87, 255), Color.TRANSPARENT) //main_blue, transparent
                )

                //백그라운드
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                    intArrayOf(Color.WHITE, Color.rgb(39, 87, 255)) //white, main_blue
                )

                //텍스트
                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                        intArrayOf(Color.rgb(39, 87, 255), Color.WHITE) //main_blue, white
                    )

                )
            }
            binding.cpsActivityListRecommended.addView(chip)

            var url_list_cat=""

            chip.setOnClickListener{
                if(chip.isChecked)
                {
                    //Toast.makeText(applicationContext, "${chip.id}번째 칩", Toast.LENGTH_LONG).show()
                    //if 이 카테고리에서->칩이 이거면->url을 위한 캣코드는 이거고->캣코드 넣은 url은 이렇게 해서 쓰레드 돌리기

                    if (getContentTypeId.equals("&contentTypeId=25")) //코스, 0..4
                    {
                        contentCat1="&cat1=C01"

                        if (chip.id==0) //힐링코스
                        {
                            //Toast.makeText(applicationContext, "${chip.id}번째 칩-코스", Toast.LENGTH_LONG).show()
                            list_card_list.clear()
                            contentCat2="&cat2=C0114"
                        }
                        else if (chip.id==1) //도보코스
                        {
                            list_card_list.clear()
                            contentCat2="&cat2=C0115"
                        }
                        else if (chip.id==2) //캠핑코스
                        {
                            list_card_list.clear()
                            contentCat2="&cat2=C0116"
                        }
                        else if (chip.id==3) //맛코스
                        {
                            contentCat2="&cat2=C0117"
                        }
                        else if (chip.id==4) //가족코스
                        {
                            contentCat2="&cat2=C0112"
                        }

                        url_list_cat=url_list+ contentCat1+ contentCat2
                        val thread = Thread(NetworkThread_list(url_list_cat))
                        thread.start() // 쓰레드 시작
                        thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        //recycler view
                        recyclerView_list=findViewById(R.id.rv_activity_list_recommended_list!!)as RecyclerView
                        recyclerView_list.layoutManager= GridLayoutManager(parent, 2)
                        //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
                        recyclerView_list.adapter=adapter

                    }
                    else if (getContentTypeId.equals("&contentTypeId=32")) //숙소, 0..8
                    {
                        contentCat1="&cat1=B02"
                        contentCat2="&cat2=B0201"

                        if (chip.id==0) //관광호텔
                        {
                            //Toast.makeText(applicationContext, "${chip.id}번째 칩-숙소", Toast.LENGTH_LONG).show()
                            list_card_list.clear()
                            contentCat3="&cat3=B02010100"
                        }
                        else if (chip.id==1) //콘도미니엄
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02010500"
                        }
                        else if (chip.id==2) //유스호스텔
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02010600"
                        }
                        else if (chip.id==3) //펜션
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02010700"
                        }
                        else if (chip.id==4) //모텔
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02010900"
                        }
                        else if (chip.id==5) //민박
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02011000"
                        }
                        else if (chip.id==6) //게스트하우스
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02011100"
                        }
                        else if (chip.id==7) //서비스드레지던스
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02011300"
                        }
                        else if (chip.id==8) //한옥
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=B02011600"
                        }

                        url_list_cat=url_list+ contentCat1+ contentCat2+ contentCat3
                        val thread = Thread(NetworkThread_list(url_list_cat))
                        thread.start() // 쓰레드 시작
                        thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력
                        //recycler view
                        recyclerView_list=findViewById(R.id.rv_activity_list_recommended_list!!)as RecyclerView
                        recyclerView_list.layoutManager= GridLayoutManager(parent, 2)
                        //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
                        recyclerView_list.adapter=adapter
                    }
                    else if (getContentTypeId.equals("&contentTypeId=39")) //음식점, 0..5
                    {
                        contentCat1="&cat1=A05"
                        contentCat2="&cat2=A0502"

                        if (chip.id==0) //한식
                        {
                            //Toast.makeText(applicationContext, "${chip.id}번째 칩-맛집", Toast.LENGTH_LONG).show()
                            list_card_list.clear()
                            contentCat3="&cat3=A05020100"
                        }
                        else if (chip.id==1) //서양식
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=A05020200"
                        }
                        else if (chip.id==2) //일식
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=A05020300"
                        }
                        else if (chip.id==3) //중식
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=A05020400"
                        }
                        else if (chip.id==4) //이색음식점
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=A05020700"
                        }
                        else if (chip.id==5) //카페/전통찻집
                        {
                            list_card_list.clear()
                            contentCat3="&cat3=A05020900"
                        }
                        url_list_cat=url_list+ contentCat1+ contentCat2+ contentCat3
                        val thread = Thread(NetworkThread_list(url_list_cat))
                        thread.start() // 쓰레드 시작
                        thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력
                        //recycler view
                        recyclerView_list=findViewById(R.id.rv_activity_list_recommended_list!!)as RecyclerView
                        recyclerView_list.layoutManager= GridLayoutManager(parent, 2)
                        //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
                        recyclerView_list.adapter=adapter
                    }


                }
                else if(!chip.isChecked)
                {
                    list_card_list.clear()
                    val thread = Thread(NetworkThread_list(url_list))
                    thread.start() // 쓰레드 시작
                    thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력
                    //recycler view
                    recyclerView_list=findViewById(R.id.rv_activity_list_recommended_list!!)as RecyclerView
                    recyclerView_list.layoutManager= GridLayoutManager(parent, 2)
                    //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
                    recyclerView_list.adapter=adapter
                }
            }

        }


    }

}




