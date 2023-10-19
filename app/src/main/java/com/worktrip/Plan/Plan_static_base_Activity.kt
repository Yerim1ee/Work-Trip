package com.worktrip.Plan

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.worktrip.DataClass.PlanWorkShopUserData
import com.worktrip.DataClass.UserBaseData
import com.worktrip.Home.DetailCourseActivity
import com.worktrip.Home.RecyclerAdapter_card_image_title
import com.worktrip.Home.data_card_list
import com.worktrip.NetworkThread_list
import com.worktrip.R
import com.worktrip.databinding.ActivityPlanStaticBaseBinding
import com.worktrip.list_card_list
import com.worktrip.list_contentId
import org.eazegraph.lib.models.PieModel

class Plan_static_base_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanStaticBaseBinding

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var docID:String

    lateinit var title:String

    var data_1: Double = 0.0
    var data_2: Double = 0.0
    var data_3: Double =0.0
    var data_4: Double = 0.0
    var data_5: Double = 0.0
    var data_6: Double = 0.0
    var data_7: Double = 0.0
    var data_8: Double = 0.0
    var whole: Double = 0.0

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
    private val arrange="&arrange=Q"
    //관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
    private var contentTypeId=""
    //카테고리
    private var contentCat1="&cat1=C01"
    private var contentCat2=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanStaticBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_static))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvStaticTitle
        toolbarTitle.text = "팀원 통계"

        docID = intent.getStringExtra("docID").toString()

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        title = "코스"
        get_static(title)
        binding.rgPlanStaticRadiogroup.setOnCheckedChangeListener { group, checkId ->
            title = findViewById<RadioButton>(checkId).text.toString()
            get_static(title)
        }

    }

    private fun setData() {

        if ((data_1 + data_2 + data_3 + data_4 + data_5 + data_6 + data_7 + data_8) == whole) {

            data_1 = (data_1 / whole) * 100
            binding.tvStatic1.setText((data_1).toInt().toString())

            data_2 = (data_2 / whole) * 100
            binding.tvStatic2.setText((data_2).toInt().toString())

            data_3 = (data_3 / whole) * 100
            binding.tvStatic3.setText((data_3).toInt().toString())

            data_4 = (data_4 / whole) * 100
            binding.tvStatic4.setText((data_4).toInt().toString())

            data_5 = (data_5 / whole) * 100
            binding.tvStatic5.setText((data_5).toInt().toString())

            data_6 = (data_6 / whole) * 100
            binding.tvStatic6.setText((data_6).toInt().toString())

            data_7 = (data_7 / whole) * 100
            binding.tvStatic7.setText((data_7).toInt().toString())

            data_8 = (data_8 / whole) * 100
            binding.tvStatic8.setText((data_8).toInt().toString())



            when(title){

                "코스" -> {
                    binding.pcStaticPiechart.clearChart()

                    binding.clPlanStatic6.visibility = View.GONE
                    binding.clPlanStatic7.visibility = View.GONE
                    binding.clPlanStatic8.visibility = View.GONE

                    binding.tvStatic1Title.setText("힐링 코스")
                    binding.tvStatic2Title.setText("도보 코스")
                    binding.tvStatic3Title.setText("맛 코스")
                    binding.tvStatic4Title.setText("캠핑 코스")
                    binding.tvStatic5Title.setText("가족 코스")
                    add_chart("힐링 코스", data_1.toFloat(), "#2757FF")
                    add_chart("도보 코스", data_2.toFloat(), "#6688FF")
                    add_chart("맛 코스", data_3.toFloat(), "#E1E8FF")
                    add_chart("캠핑 코스", data_4.toFloat(), "#FF7070")
                    add_chart("가족 코스", data_5.toFloat(), "#FFA9A9")


                    //추가
                    var max=arrayOf(data_1, data_2, data_3, data_4, data_5).max()
                    var maxKeyword=""

                    binding.llPlanBaseCourse.visibility = View.VISIBLE
                    lateinit var recyclerView_course: RecyclerView

                    if (max==data_1)
                    {
                        list_card_list.clear()

                        val list_healedCourse: ArrayList<data_card_list> = ArrayList()
                        var adapter_healedCourse= RecyclerAdapter_card_image_title(list_healedCourse)
                        //lateinit var recyclerView_healedCourse: RecyclerView

                        maxKeyword+="#힐링코스 "
                        binding.llPlanStaticHealed.visibility= View.VISIBLE

                        contentCat2 = "&cat2=C0114"
                        get_course(contentCat2)
                        contentCat2=""

                        var i=0
                        while (i <= 2)
                        {
                            var randomCourse= list_card_list.random()

                            if (!(randomCourse.typeid.equals("25"))) //혹시 코스 외 다른 카테고리의 데이터가 들어온다면
                            {
                                list_card_list.remove(randomCourse)
                                --i
                                continue
                            }

                            list_healedCourse.add(randomCourse)
                            list_card_list.remove(randomCourse) //중복 방지용 삭제

                            if (list_healedCourse.lastOrNull()==null)
                            {
                                list_healedCourse.add(data_card_list("null", "목록을 로드하지 못했습니다.", "", "", ""))
                            }
                            ++i
                        }

                        recyclerView_course=binding.rvActivityPlanStaticHealedCourse as RecyclerView
                        recyclerView_course.layoutManager=
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        recyclerView_course.adapter=adapter_healedCourse

                        adapter_healedCourse.setOnClickListener( object : RecyclerAdapter_card_image_title.ItemClickListener{
                            override fun onClick(view: View, position: Int) {
                                var intent_course = Intent(this@Plan_static_base_Activity, DetailCourseActivity::class.java)

                                intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                                intent_course.putExtra("contentId", list_healedCourse[position].id)

                                startActivity(intent_course)
                                list_contentId =""
                                contentTypeId =""
                            }
                        })
                    }
                    if (max==data_2)
                    {
                        list_card_list.clear()

                        val list_walkCourse: ArrayList<data_card_list> = ArrayList()
                        var adapter_walkCourse= RecyclerAdapter_card_image_title(list_walkCourse)
                        //lateinit var recyclerView_walkCourse: RecyclerView

                        maxKeyword+="#도보코스 "
                        binding.llPlanStaticWalk.visibility= View.VISIBLE

                        contentCat2 = "&cat2=C0115"
                        get_course(contentCat2)
                        contentCat2=""

                        var i=0
                        while (i <= 2)
                        {
                            var randomCourse= list_card_list.random()

                            if (!(randomCourse.typeid.equals("25"))) //혹시 코스 외 다른 카테고리의 데이터가 들어온다면
                            {
                                list_card_list.remove(randomCourse)
                                --i
                                continue
                            }

                            list_walkCourse.add(randomCourse)
                            list_card_list.remove(randomCourse) //중복 방지용 삭제

                            if (list_walkCourse.lastOrNull()==null)
                            {
                                list_walkCourse.add(data_card_list("null", "목록을 로드하지 못했습니다.", "", "", ""))
                            }
                            ++i
                        }

                        recyclerView_course=binding.rvActivityPlanStaticWalkCourse as RecyclerView
                        recyclerView_course.layoutManager=
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        recyclerView_course.adapter=adapter_walkCourse

                        adapter_walkCourse.setOnClickListener( object : RecyclerAdapter_card_image_title.ItemClickListener{
                            override fun onClick(view: View, position: Int) {
                                var intent_course = Intent(this@Plan_static_base_Activity, DetailCourseActivity::class.java)

                                intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                                intent_course.putExtra("contentId", list_walkCourse[position].id)

                                startActivity(intent_course)
                                list_contentId =""
                                contentTypeId =""
                            }
                        })
                    }
                    if (max==data_3)
                    {
                        list_card_list.clear()


                        val list_eatCourse: ArrayList<data_card_list> = ArrayList()
                        var adapter_eatCourse= RecyclerAdapter_card_image_title(list_eatCourse)
                        //lateinit var recyclerView_eatCourse: RecyclerView

                        maxKeyword+="#맛코스 "
                        binding.llPlanStaticEat.visibility= View.VISIBLE

                        contentCat2 = "&cat2=C0117"
                        get_course(contentCat2)
                        contentCat2=""

                        var i=0
                        while (i <= 2)
                        {
                            var randomCourse= list_card_list.random()

                            if (!(randomCourse.typeid.equals("25"))) //혹시 코스 외 다른 카테고리의 데이터가 들어온다면
                            {
                                list_card_list.remove(randomCourse)
                                --i
                                continue
                            }

                            list_eatCourse.add(randomCourse)
                            list_card_list.remove(randomCourse) //중복 방지용 삭제

                            if (list_eatCourse.lastOrNull()==null)
                            {
                                list_eatCourse.add(data_card_list("null", "목록을 로드하지 못했습니다.", "", "", ""))
                            }
                            ++i
                        }

                        recyclerView_course=binding.rvActivityPlanStaticEatCourse as RecyclerView
                        recyclerView_course.layoutManager=
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        recyclerView_course.adapter=adapter_eatCourse

                        adapter_eatCourse.setOnClickListener( object : RecyclerAdapter_card_image_title.ItemClickListener{
                            override fun onClick(view: View, position: Int) {
                                var intent_course = Intent(this@Plan_static_base_Activity, DetailCourseActivity::class.java)

                                intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                                intent_course.putExtra("contentId", list_eatCourse[position].id)

                                startActivity(intent_course)
                                list_contentId =""
                                contentTypeId =""
                            }
                        })

                    }
                    if (max==data_4)
                    {
                        list_card_list.clear()

                        val list_campingCourse: ArrayList<data_card_list> = ArrayList()
                        var adapter_campCourse= RecyclerAdapter_card_image_title(list_campingCourse)
                        //lateinit var recyclerView_campCourse: RecyclerView

                        maxKeyword+="#캠핑코스 "
                        binding.llPlanStaticCamping.visibility= View.VISIBLE

                        contentCat2 = "&cat2=C0116"
                        get_course(contentCat2)
                        contentCat2=""

                        var i=0
                        while (i <= 2)
                        {
                            var randomCourse= list_card_list.random()

                            if (!(randomCourse.typeid.equals("25"))) //혹시 코스 외 다른 카테고리의 데이터가 들어온다면
                            {
                                list_card_list.remove(randomCourse)
                                --i
                                continue
                            }

                            list_campingCourse.add(randomCourse)
                            list_card_list.remove(randomCourse) //중복 방지용 삭제

                            if (list_campingCourse.lastOrNull()==null)
                            {
                                list_campingCourse.add(data_card_list("null", "목록을 로드하지 못했습니다.", "", "", ""))
                            }
                            ++i
                        }

                        recyclerView_course=binding.rvActivityPlanStaticCampingCourse as RecyclerView
                        recyclerView_course.layoutManager=
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        recyclerView_course.adapter=adapter_campCourse

                        adapter_campCourse.setOnClickListener( object : RecyclerAdapter_card_image_title.ItemClickListener{
                            override fun onClick(view: View, position: Int) {
                                var intent_course = Intent(this@Plan_static_base_Activity, DetailCourseActivity::class.java)

                                intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                                intent_course.putExtra("contentId", list_campingCourse[position].id)

                                startActivity(intent_course)
                                list_contentId =""
                                contentTypeId =""
                            }
                        })
                    }
                    if (max==data_5)
                    {
                        list_card_list.clear()

                        val list_familyCourse: ArrayList<data_card_list> = ArrayList()
                        var adapter_familyCourse= RecyclerAdapter_card_image_title(list_familyCourse)
                        //lateinit var recyclerView_familyCourse: RecyclerView

                        maxKeyword+="#가족코스 "
                        binding.llPlanStaticFamily.visibility= View.VISIBLE

                        contentCat2 = "&cat2=C0112"
                        get_course(contentCat2)
                        contentCat2=""

                        var i=0
                        while (i <= 2)
                        {
                            var randomCourse= list_card_list.random()

                            if (!(randomCourse.typeid.equals("25"))) //혹시 코스 외 다른 카테고리의 데이터가 들어온다면
                            {
                                list_card_list.remove(randomCourse)
                                --i
                                continue
                            }

                            list_familyCourse.add(randomCourse)
                            list_card_list.remove(randomCourse) //중복 방지용 삭제

                            if (list_familyCourse.lastOrNull()==null)
                            {
                                list_familyCourse.add(data_card_list("null", "목록을 로드하지 못했습니다.", "", "", ""))
                            }
                            ++i
                        }

                        recyclerView_course=binding.rvActivityPlanStaticFamilyCourse as RecyclerView
                        recyclerView_course.layoutManager=
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        recyclerView_course.adapter=adapter_familyCourse

                        adapter_familyCourse.setOnClickListener( object : RecyclerAdapter_card_image_title.ItemClickListener{
                            override fun onClick(view: View, position: Int) {
                                var intent_course = Intent(this@Plan_static_base_Activity, DetailCourseActivity::class.java)

                                intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                                intent_course.putExtra("contentId", list_familyCourse[position].id)

                                startActivity(intent_course)
                                list_contentId =""
                                contentTypeId =""
                            }
                        })
                    }

                    //가장 많은 코스
                    binding.tvStaticKeyword.setText(maxKeyword)

                }
                "맛집" -> {
                    binding.pcStaticPiechart.clearChart()
                    binding.llPlanBaseCourse.visibility = View.GONE

                    binding.clPlanStatic6.visibility = View.VISIBLE
                    binding.clPlanStatic7.visibility = View.GONE
                    binding.clPlanStatic8.visibility = View.GONE

                    var max=arrayOf(data_1, data_2, data_3, data_4, data_5, data_6, data_7, data_8).max()
                    var maxKeyword=""
                    if(max == data_1){
                        maxKeyword +="#한식 "
                    }
                    if(max == data_2){
                        maxKeyword +="#서양식 "
                    }
                    if(max == data_3){
                        maxKeyword +="#중식 "
                    }
                    if(max == data_4){
                        maxKeyword +="#일식 "
                    }
                    if(max == data_5){
                        maxKeyword +="#이색음식점 "
                    }
                    if(max == data_6){
                        maxKeyword +="#카페/전통찻집 "
                    }
                    binding.tvStaticKeyword.setText(maxKeyword)

                    binding.tvStatic1Title.setText("한식")
                    binding.tvStatic2Title.setText("서양식")
                    binding.tvStatic3Title.setText("중식")
                    binding.tvStatic4Title.setText("일식")
                    binding.tvStatic5Title.setText("이색음식점")
                    binding.tvStatic6Title.setText("카페/전통찻집")
                    add_chart("한식", data_1.toFloat(), "#2757FF")
                    add_chart("서양식", data_2.toFloat(), "#6688FF")
                    add_chart("중식", data_3.toFloat(), "#E1E8FF")
                    add_chart("일식", data_4.toFloat(), "#FF7070")
                    add_chart("이색음식점", data_5.toFloat(), "#FFA9A9")
                    add_chart("카페/전통찻집", data_6.toFloat(), "#FFDADA")
                }

                "관광지" -> {
                    binding.pcStaticPiechart.clearChart()
                    binding.llPlanBaseCourse.visibility = View.GONE
                    binding.clPlanStatic6.visibility = View.VISIBLE
                    binding.clPlanStatic7.visibility = View.VISIBLE
                    binding.clPlanStatic8.visibility = View.GONE
                    var max=arrayOf(data_1, data_2, data_3, data_4, data_5, data_6, data_7, data_8).max()
                    var maxKeyword=""
                    if(max == data_1){
                        maxKeyword +="#자연관광지 "
                    }
                    if(max == data_2){
                        maxKeyword +="#관광자원 "
                    }
                    if(max == data_3){
                        maxKeyword +="#역사관광지 "
                    }
                    if(max == data_4){
                        maxKeyword +="#체험관광지 "
                    }
                    if(max == data_5){
                        maxKeyword +="#산업관광지 "
                    }
                    if(max == data_6){
                        maxKeyword +="#휴양관광지 "
                    }
                    if(max == data_7){
                        maxKeyword +="#건축/조형물 "
                    }
                    binding.tvStaticKeyword.setText(maxKeyword)

                    binding.tvStatic1Title.setText("자연관광지")
                    binding.tvStatic2Title.setText("관광자원")
                    binding.tvStatic3Title.setText("역사관광지")
                    binding.tvStatic4Title.setText("체험관광지")
                    binding.tvStatic5Title.setText("산업관광지")
                    binding.tvStatic6Title.setText("휴양관광지")
                    binding.tvStatic7Title.setText("건축/조형물")
                    add_chart("자연관광지", data_1.toFloat(), "#2757FF")
                    add_chart("관광자원", data_2.toFloat(), "#6688FF")
                    add_chart("역사관광지", data_3.toFloat(), "#E1E8FF")
                    add_chart("체험관광지", data_4.toFloat(), "#FF7070")
                    add_chart("산업관광지", data_5.toFloat(), "#FFA9A9")
                    add_chart("휴양관광지", data_6.toFloat(), "#FFDADA")
                    add_chart("건축/조형물", data_7.toFloat(), "#6E6E73")
                }

                "숙박" -> {
                    binding.pcStaticPiechart.clearChart()
                    binding.llPlanBaseCourse.visibility = View.GONE
                    binding.clPlanStatic6.visibility = View.VISIBLE
                    binding.clPlanStatic7.visibility = View.VISIBLE
                    binding.clPlanStatic8.visibility = View.VISIBLE

                    var max=arrayOf(data_1, data_2, data_3, data_4, data_5, data_6, data_7, data_8).max()
                    var maxKeyword=""
                    if(max == data_1){
                        maxKeyword +="#한옥 "
                    }
                    if(max == data_2){
                        maxKeyword +="#게스트하우스 "
                    }
                    if(max == data_3){
                        maxKeyword +="#펜션 "
                    }
                    if(max == data_4){
                        maxKeyword +="#관광호텔 "
                    }
                    if(max == data_5){
                        maxKeyword +="#모텔 "
                    }
                    if(max == data_6){
                        maxKeyword +="#유스호스텔 "
                    }
                    if(max == data_7){
                        maxKeyword +="#민박 "
                    }
                    if(max == data_8){
                        maxKeyword +="#콘도미디엄 "
                    }
                    binding.tvStaticKeyword.setText(maxKeyword)


                    binding.tvStatic1Title.setText("한옥")
                    binding.tvStatic2Title.setText("게스트하우스")
                    binding.tvStatic3Title.setText("펜션")
                    binding.tvStatic4Title.setText("관광호텔")
                    binding.tvStatic5Title.setText("모텔")
                    binding.tvStatic6Title.setText("유스호스텔")
                    binding.tvStatic7Title.setText("민박")
                    binding.tvStatic8Title.setText("콘도미디엄")
                    add_chart("한옥", data_1.toFloat(), "#2757FF")
                    add_chart("게스트하우스", data_2.toFloat(), "#6688FF")
                    add_chart("펜션", data_3.toFloat(), "#E1E8FF")
                    add_chart("관광호텔", data_4.toFloat(), "#FF7070")
                    add_chart("모텔", data_5.toFloat(), "#FFA9A9")
                    add_chart("유스호스텔", data_6.toFloat(), "#FFDADA")
                    add_chart("민박", data_7.toFloat(), "#6E6E73")
                    add_chart("콘도미디엄", data_7.toFloat(), "#BBBBBE")

                }
                "레포츠" -> {
                    binding.pcStaticPiechart.clearChart()
                    binding.llPlanBaseCourse.visibility = View.GONE
                    binding.clPlanStatic6.visibility = View.GONE
                    binding.clPlanStatic7.visibility = View.GONE
                    binding.clPlanStatic8.visibility = View.GONE

                    var max=arrayOf(data_1, data_2, data_3, data_4, data_5, data_6, data_7, data_8).max()
                    var maxKeyword=""
                    if(max == data_1){
                        maxKeyword +="#선호X "
                    }
                    if(max == data_2){
                        maxKeyword +="#육상레포츠 "
                    }
                    if(max == data_3){
                        maxKeyword +="#수상레포츠 "
                    }
                    if(max == data_4){
                        maxKeyword +="#복합레포츠 "
                    }
                    if(max == data_5){
                        maxKeyword +="#항공레포츠 "
                    }
                    binding.tvStaticKeyword.setText(maxKeyword)

                    binding.tvStatic1Title.setText("선호X")
                    binding.tvStatic2Title.setText("육상레포츠")
                    binding.tvStatic3Title.setText("수상레포츠")
                    binding.tvStatic4Title.setText("복합레포츠")
                    binding.tvStatic5Title.setText("항공레포츠")
                    add_chart("선호X", data_1.toFloat(), "#2757FF")
                    add_chart("육상레포츠", data_2.toFloat(), "#6688FF")
                    add_chart("수상레포츠", data_3.toFloat(), "#E1E8FF")
                    add_chart("복합레포츠", data_4.toFloat(), "#FF7070")
                    add_chart("항공레포츠", data_5.toFloat(), "#FFA9A9")

                }
            }


        }
    }

    private fun get_static(title: String){

        whole = 0.0
        data_1 = 0.0
        data_2 = 0.0
        data_3 = 0.0
        data_4 = 0.0
        data_5 = 0.0
        data_6 = 0.0
        data_7 = 0.0
        data_8 = 0.0

        // 인원수 측정
        db.collection("user_workshop")
            .get()
            .addOnSuccessListener { result1 ->
                for (document1 in result1) {
                    db.collection("user_workshop")
                        .document(document1.id)
                        .collection("workshop_list")
                        .whereEqualTo("workshop_docID", docID)
                        .get()
                        .addOnSuccessListener { result2 ->
                            for (document2 in result2) {
                                whole = whole + 1
                            }
                        }
                }
            }

        // 각 영역의 값 누적하며 구하기
        db.collection("user_workshop")
            .get()
            .addOnSuccessListener { result1 ->
                for (document1 in result1) {
                    db.collection("user_workshop")
                        .document(document1.id)
                        .collection("workshop_list")
                        .whereEqualTo("workshop_docID", docID)
                        .get()
                        .addOnSuccessListener { result2 ->
                            for (document2 in result2){
                                val item1 = document2.toObject(PlanWorkShopUserData::class.java)
                                db.collection("user")
                                    .document(item1.uID.toString())
                                    .get()
                                    .addOnSuccessListener {result3 ->
                                        val item2 = result3.toObject(UserBaseData::class.java)
                                        if (item2 != null) {



                                            when(title){
                                                "코스" -> {
                                                    when(item2.course){
                                                        "캠핑 코스" ->{ data_1 = data_1 + 1 }
                                                        "도보 코스"->{ data_2 = data_2 + 1 }
                                                        "맛 코스"->{ data_3 = data_3 + 1 }
                                                        "힐링 코스"->{ data_4 = data_4 + 1 }
                                                        "가족 코스" -> { data_5 = data_5 + 1 }
                                                    }
                                                    setData()

                                                }
                                                "맛집" -> {
                                                    when(item2.food){
                                                        "한식" ->{ data_1 = data_1 + 1 }
                                                        "서양식"->{ data_2 = data_2 + 1 }
                                                        "중식"->{ data_3 = data_3 + 1 }
                                                        "일식"->{ data_4 = data_4 + 1 }
                                                        "이색음식점" -> { data_5 = data_5 + 1 }
                                                        "카페/전통찻집" -> { data_6 = data_6 + 1 }
                                                    }
                                                    setData()
                                                }

                                                "관광지" -> {
                                                    when(item2.travel){
                                                        "자연관광지" ->{ data_1 = data_1 + 1 }
                                                        "관광자원"->{ data_2 = data_2 + 1 }
                                                        "역사관광지"->{ data_3 = data_3 + 1 }
                                                        "체험관광지"->{ data_4 = data_4 + 1 }
                                                        "산업관광지" -> { data_5 = data_5 + 1 }
                                                        "휴양관광지" -> { data_6 = data_6 + 1 }
                                                        "건축/조형물" -> { data_7 = data_7 + 1 }
                                                    }
                                                    setData()

                                                }

                                                "숙박" -> {
                                                    when(item2.sleep){
                                                        "한옥" ->{ data_1 = data_1 + 1 }
                                                        "게스트하우스"->{ data_2 = data_2 + 1 }
                                                        "펜션"->{ data_3 = data_3 + 1 }
                                                        "관광호텔"->{ data_4 = data_4 + 1 }
                                                        "모텔" -> { data_5 = data_5 + 1 }
                                                        "유스호스텔" -> { data_6 = data_6 + 1 }
                                                        "민박" -> { data_7 = data_7 + 1 }
                                                        "콘도미디엄" -> { data_8 = data_8 + 1 }
                                                    }
                                                    setData()

                                                }
                                                "레포츠" -> {
                                                    when(item2.reports){
                                                        "선호X" ->{ data_1 = data_1 + 1 }
                                                        "육상레포츠"->{ data_2 = data_2 + 1 }
                                                        "수상레포츠"->{ data_3 = data_3 + 1 }
                                                        "복합레포츠"->{ data_4 = data_4 + 1 }
                                                        "항공레포츠" -> { data_5 = data_5 + 1 }
                                                    }
                                                    setData()

                                                }
                                            }

                                        }
                                    }
                            }
                        }
                }
            }
    }

    private fun get_course(cat2: String){
        //API 정보를 가지고 있는 주소
        var url_list = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + key + numOfRows + mobileOS + mobileApp + _type + listYN + arrange + "&contentTypeId=25" + contentCat1 + cat2

        //쓰레드 생성
        val threadList = Thread(NetworkThread_list(url_list))
        threadList.start() // 쓰레드 시작
        threadList.join() // 멀티 작업 안되게 하려면 start 후 join 입력
    }

    private fun add_chart(label:String, value:Float, color:String){
        binding.pcStaticPiechart
            .addPieSlice(
                PieModel(
                    label, value,
                    Color.parseColor(color)
                )
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onDestroy() {
        super.onDestroy()
        //초기화
        list_card_list.clear()
    }

}