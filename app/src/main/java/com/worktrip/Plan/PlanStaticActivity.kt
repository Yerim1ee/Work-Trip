package com.worktrip.Plan

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worktrip.DataClass.PlanWorkShopUserData
import com.worktrip.DataClass.UserBaseData
import com.worktrip.R
import com.worktrip.databinding.ActivityPlanStaticBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.worktrip.Home.DetailCourseActivity
import com.worktrip.Home.RecyclerAdapter_card_image_title
import com.worktrip.Home.data_card_list
import com.worktrip.NetworkThread_list
import com.worktrip.list_card_list
import com.worktrip.list_contentId
import org.eazegraph.lib.models.PieModel

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

class PlanStaticActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanStaticBinding

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    var camping: Double = 0.0
    var healed: Double = 0.0
    var eat: Double =0.0
    var family: Double = 0.0
    var walk: Double = 0.0
    var whole: Double = 0.0

    lateinit var docID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanStaticBinding.inflate(layoutInflater)
        setContentView(binding.root)

        docID = intent.getStringExtra("docID").toString()

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_static))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvStaticTitle
        toolbarTitle.text = "팀원 통계"

        get_static()

        Toast.makeText(applicationContext, "통계를 불러오는 중입니다.", Toast.LENGTH_LONG).show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData() {

        if((camping + eat + healed + family + walk) == whole){


                    camping = (camping/whole)*100
                    binding.tvStaticCamping.setText((camping).toInt().toString())




                    walk = (walk/whole)*100
                    binding.tvStaticWalk.setText((walk).toInt().toString())




                    eat = (eat/whole)*100
                    binding.tvStaticEat.setText((eat).toInt().toString())




                    healed = (healed/whole)*100
                    binding.tvStaticHealed.setText((healed).toInt().toString())




                    family = (family/whole)*100
                    binding.tvStaticFamily.setText((family).toInt().toString())

            // Set the data and color to the pie chart
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "힐링", binding.tvStaticHealed.text.toString().toFloat(),
                        Color.parseColor("#2757FF")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "맛", binding.tvStaticEat.text.toString().toFloat(),
                        Color.parseColor("#E1E8FF")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "캠핑", binding.tvStaticCamping.text.toString().toFloat(),
                        Color.parseColor("#FFA9A9")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "도보", binding.tvStaticWalk.text.toString().toFloat(),
                        Color.parseColor("#6688FF")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "가족", binding.tvStaticFamily.text.toString().toFloat(),
                        Color.parseColor("#FFDADA")
                    )
                )

            //추가
            var max=arrayOf(healed, walk, eat, camping, family).max()
            var maxKeyword=""

            lateinit var recyclerView_course: RecyclerView

            if (max==healed)
            {
                list_card_list.clear()

                val list_healedCourse: ArrayList<data_card_list> = ArrayList()
                var adapter_healedCourse=RecyclerAdapter_card_image_title(list_healedCourse)
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
                        var intent_course = Intent(this@PlanStaticActivity, DetailCourseActivity::class.java)

                        intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                        intent_course.putExtra("contentId", list_healedCourse[position].id)

                        startActivity(intent_course)
                        list_contentId =""
                        contentTypeId =""
                    }
                })
            }
            if (max==walk)
            {
                list_card_list.clear()

                val list_walkCourse: ArrayList<data_card_list> = ArrayList()
                var adapter_walkCourse=RecyclerAdapter_card_image_title(list_walkCourse)
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
                        var intent_course = Intent(this@PlanStaticActivity, DetailCourseActivity::class.java)

                        intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                        intent_course.putExtra("contentId", list_walkCourse[position].id)

                        startActivity(intent_course)
                        list_contentId =""
                        contentTypeId =""
                    }
                })
            }
            if (max==eat)
            {
                list_card_list.clear()

                val list_eatCourse: ArrayList<data_card_list> = ArrayList()
                var adapter_eatCourse=RecyclerAdapter_card_image_title(list_eatCourse)
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
                        var intent_course = Intent(this@PlanStaticActivity, DetailCourseActivity::class.java)

                        intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                        intent_course.putExtra("contentId", list_eatCourse[position].id)

                        startActivity(intent_course)
                        list_contentId =""
                        contentTypeId =""
                    }
                })

            }
            if (max==camping)
            {
                list_card_list.clear()

                val list_campingCourse: ArrayList<data_card_list> = ArrayList()
                var adapter_campCourse=RecyclerAdapter_card_image_title(list_campingCourse)
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
                        var intent_course = Intent(this@PlanStaticActivity, DetailCourseActivity::class.java)

                        intent_course.putExtra("contentTypeId", "&contentTypeId=25")
                        intent_course.putExtra("contentId", list_campingCourse[position].id)

                        startActivity(intent_course)
                        list_contentId =""
                        contentTypeId =""
                    }
                })
            }
            if (max==family)
            {
                list_card_list.clear()

                val list_familyCourse: ArrayList<data_card_list> = ArrayList()
                var adapter_familyCourse=RecyclerAdapter_card_image_title(list_familyCourse)
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
                        var intent_course = Intent(this@PlanStaticActivity, DetailCourseActivity::class.java)

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

    }

    private fun get_course(cat2: String){
        //API 정보를 가지고 있는 주소
        var url_list = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + key + numOfRows + mobileOS + mobileApp + _type + listYN + arrange + "&contentTypeId=25" + contentCat1 + cat2

        //쓰레드 생성
        val threadList = Thread(NetworkThread_list(url_list))
        threadList.start() // 쓰레드 시작
        threadList.join() // 멀티 작업 안되게 하려면 start 후 join 입력
    }

    private fun get_static(){

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
                                            when(item2.course){
                                                "캠핑 코스" ->{
                                                    camping = camping + 1
                                                }

                                                "도보 코스"->{
                                                    walk = walk + 1
                                                }

                                                "맛 코스"->{
                                                    eat = eat + 1
                                                }

                                                "힐링 코스"->{
                                                    healed = healed + 1
                                                }

                                                "가족 코스" -> {
                                                    family = family + 1
                                                }
                                            }
                                            setData()
                                        }


                                    }

                            }

                        }
                }

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        //초기화
        list_card_list.clear()
    }

}