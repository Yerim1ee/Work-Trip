package com.example.worktrip.Home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.NetworkThread_list
import com.example.worktrip.NetworkThread_searchKeyword1
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityHomeSearchListBinding
import com.example.worktrip.list_card_list
import com.example.worktrip.list_contentId
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.regex.Pattern

private lateinit var binding : ActivityHomeSearchListBinding

private lateinit var recyclerView_list: RecyclerView
private lateinit var recyclerView_list_people: RecyclerView
private val list_card_list_people: ArrayList<data_card_list_people> = ArrayList()

var getHomeSearchText=""
var getHomeSearchCategory=""
var getHomeSearchLocation=""

private val key="599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
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
//검색어
private lateinit var keyword: String
//관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
private var contentTypeId=""

var url_search=""

class HomeSearchListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeSearchListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_home_search_list)

        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_home_search_list))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        getHomeSearchText = intent.getStringExtra("searchText").toString()
        getHomeSearchCategory = intent.getStringExtra("searchCategory").toString()
        getHomeSearchLocation = intent.getStringExtra("searchLocation").toString()

        //text view
        binding.tvActivityHomeSearchListText.setText(getHomeSearchText)
        binding.tvActivityHomeSearchListCategory.setText(intent.getStringExtra("searchCategoryView").toString())
        binding.tvActivityHomeSearchListLocation.setText(intent.getStringExtra("searchLocationView").toString())

        //adapter
        var adapter_people=RecyclerAdapter_card_list_people(list_card_list_people)
        var adapter_list=RecyclerAdapter_card_list(list_card_list)

        //intent
        var courseIntent=Intent(this, DetailCourseActivity::class.java)
        var lodgingIntent=Intent(this, DetailLodgingActivity::class.java)
        var foodIntent=Intent(this, DetailFoodActivity::class.java)
        //카테고리 선택 하지 않을 시 관광 명소 등이 나올 때도 있으므로 나중에 추가할 것 - 선택하지 않음 옵션을 지워서 해결

        /*if (getHomeSearchCategory.equals("선택하지 않음"))
        {

            firestore_bookmark_list.collection("category_program")
                .get()
                .addOnSuccessListener { task ->
                    for (document in task) {

                        if (document.data["title"].toString().contains(getHomeSearchText))
                        {
                            programId = document.data["id"].toString() //필드 데이터
                            programKeyword = document.data["keyword"].toString() //필드 데이터
                            programTitle = document.data["title"].toString() //필드 데이터
                            programImg = document.data["image"].toString() //필드 데이터
                            programPeople = document.data["people"].toString() //필드 데이터
                            programOverview = document.data["overview"].toString() //필드 데이터

                            list_card_list_people.add(
                                data_card_list_people(
                                    programId,
                                    programKeyword,
                                    programTitle,
                                    programImg,
                                    programPeople,
                                    programOverview))
                        }
                    }

                    //recycler view
                    recyclerView_list_people =
                        findViewById(R.id.rv_activity_home_search_list2!!) as RecyclerView
                    recyclerView_list_people.layoutManager = GridLayoutManager(this, 2)
                    recyclerView_list_people.adapter = adapter_people

                    if (list_card_list_people.size==0&& list_card_list.size==0)
                    {
                        Toast.makeText(applicationContext, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show()
                    }

                    var intent = Intent(this, DetailProgramActivity::class.java)

                    adapter_people.setOnClickListener(object :
                        RecyclerAdapter_card_list_people.ItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                            adapter_people.setOnClickListener(object :
                                RecyclerAdapter_card_list_people.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                                    intent.putExtra("contentId", list_card_list_people[position].id)

                                    startActivity(intent)
                                    //contentId = ""
                                }
                            })
                        }
                    })
                }
//검색어 한글 판별
            var isKOR=Pattern.compile(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*").matcher(getHomeSearchText)
            if(isKOR.find()) {
                encodingKOR(getHomeSearchText) //한글 인코딩
            }

            keyword="&keyword="+ getHomeSearchText

            if (!(getHomeSearchCategory.equals("선택하지 않음")))
            {
                contentTypeId = getHomeSearchCategory
            }

            url_search="https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey="+key+ mobileOS+ mobileApp+ _type+ listYN+ arrange+ keyword+ contentTypeId

            //쓰레드 생성
            val thread = Thread(NetworkThread_searchKeyword1(url_search, getHomeSearchLocation))
            thread.start() // 쓰레드 시작
            thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력

            //recycler view
            recyclerView_list=findViewById(R.id.rv_activity_home_search_list!!)as RecyclerView
            recyclerView_list.layoutManager= GridLayoutManager(parent, 2)
            //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
            recyclerView_list.adapter=adapter_list

            adapter_list.setOnClickListener( object : RecyclerAdapter_card_list.ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                    if (list_card_list[position].typeid.equals("25")) //코스
                    {
                        //courseIntent.putExtra("contentTypeId", list_card_list[position].typeid)
                        courseIntent.putExtra("contentId", list_card_list[position].id)

                        startActivity(courseIntent)
                    }
                    else if (list_card_list[position].typeid.equals("32")) //숙박
                    {
                        //lodgingIntent.putExtra("contentTypeId", list_card_list[position].typeid)
                        lodgingIntent.putExtra("contentId", list_card_list[position].id)

                        startActivity(lodgingIntent)
                    }
                    else if (list_card_list[position].typeid.equals("39")) //맛집
                    {
                        //foodIntent.putExtra("contentTypeId", list_card_list[position].typeid)
                        foodIntent.putExtra("contentId", list_card_list[position].id)

                        startActivity(foodIntent)
                    }

                    list_contentId =""
                    contentTypeId=""
                }
            })
        }
        else*/ if (getHomeSearchCategory.equals("program")) //firebase 검색
        {
            //recyclerView
            firestore_bookmark_list.collection("category_program")
                .get()
                .addOnSuccessListener { task ->
                    for (document in task) {

                        if (document.data["title"].toString().contains(getHomeSearchText))
                        {
                            programId = document.data["id"].toString() //필드 데이터
                            programKeyword = document.data["keyword"].toString() //필드 데이터
                            programTitle = document.data["title"].toString() //필드 데이터
                            programImg = document.data["image"].toString() //필드 데이터
                            programPeople = document.data["people"].toString() //필드 데이터
                            programOverview = document.data["overview"].toString() //필드 데이터

                            list_card_list_people.add(
                                data_card_list_people(
                                    programId,
                                    programKeyword,
                                    programTitle,
                                    programImg,
                                    programPeople,
                                    programOverview))
                    }
                    }

                    //recycler view
                    recyclerView_list_people =
                        findViewById(R.id.rv_activity_home_search_list!!) as RecyclerView
                    recyclerView_list_people.layoutManager = GridLayoutManager(this, 2)
                    recyclerView_list_people.adapter = adapter_people

                    if (list_card_list_people.size==0)
                    {
                        Toast.makeText(applicationContext, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show()
                    }

                    var intent = Intent(this, DetailProgramActivity::class.java)

                    adapter_people.setOnClickListener(object :
                        RecyclerAdapter_card_list_people.ItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                            adapter_people.setOnClickListener(object :
                                RecyclerAdapter_card_list_people.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                                    intent.putExtra("contentId", list_card_list_people[position].id)

                                    startActivity(intent)
                                    //contentId = ""
                                }
                            })
                        }
                    })


                }
        }

        else //api 사용
        {
            //검색어 한글 판별
            var isKOR=Pattern.compile(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*").matcher(getHomeSearchText)
            if(isKOR.find()) {
                encodingKOR(getHomeSearchText) //한글 인코딩
            }

            keyword="&keyword="+ getHomeSearchText

            if (!(getHomeSearchCategory.equals("선택하지 않음")))
            {
                contentTypeId = getHomeSearchCategory
            }

            url_search="https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey="+key+ mobileOS+ mobileApp+ _type+ listYN+ arrange+ keyword+ contentTypeId

            //쓰레드 생성
            val thread = Thread(NetworkThread_searchKeyword1(url_search, getHomeSearchLocation))
            thread.start() // 쓰레드 시작
            thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력

            //recycler view
            recyclerView_list=findViewById(R.id.rv_activity_home_search_list!!)as RecyclerView
            recyclerView_list.layoutManager= GridLayoutManager(parent, 2)
            //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
            recyclerView_list.adapter=adapter_list

            if (list_card_list.size==0)
            {
                Toast.makeText(applicationContext, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show()
            }

            adapter_list.setOnClickListener( object : RecyclerAdapter_card_list.ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                    if (list_card_list[position].typeid.equals("25")) //코스
                    {
                        //courseIntent.putExtra("contentTypeId", list_card_list[position].typeid)
                        courseIntent.putExtra("contentId", list_card_list[position].id)

                        startActivity(courseIntent)
                    }
                    else if (list_card_list[position].typeid.equals("32")) //숙박
                    {
                        //lodgingIntent.putExtra("contentTypeId", list_card_list[position].typeid)
                        lodgingIntent.putExtra("contentId", list_card_list[position].id)

                        startActivity(lodgingIntent)
                    }
                    else if (list_card_list[position].typeid.equals("39")) //맛집
                    {
                        //foodIntent.putExtra("contentTypeId", list_card_list[position].typeid)
                        foodIntent.putExtra("contentId", list_card_list[position].id)

                        startActivity(foodIntent)
                    }

                    list_contentId =""
                    contentTypeId=""
                }
            })
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        //초기화
        list_card_list.clear()
        list_card_list_people.clear()
    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_null, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()

                //초기화
                list_card_list.clear()
                list_card_list_people.clear()
                return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

private fun encodingKOR(str_data: String) {
    var str_encode = URLEncoder.encode(str_data, "UTF-8")
    //인코딩 : str_encode

    //url 디코딩 수행 실시
    //var str_decode = URLDecoder.decode(str_encode, "UTF-8")
    //디코딩 : +str_decode

    getHomeSearchText=str_encode
}