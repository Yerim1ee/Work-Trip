package com.example.worktrip.Home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worktrip.BottomsheetShareUrl
import com.example.worktrip.Home.RecyclerAdapter_detail_course
import com.example.worktrip.NetworkThread_categoryCode1
import com.example.worktrip.NetworkThread_detailCommon1
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityDetailCourseBinding
import com.example.worktrip.databinding.ActivityListRecommendedBinding
//import com.example.worktrip.detail_bitmap
import com.example.worktrip.detail_contentCat1
import com.example.worktrip.detail_contentCat2
import com.example.worktrip.detail_contentCat3
import com.example.worktrip.detail_contentKeyword
import com.example.worktrip.detail_contentLocation
import com.example.worktrip.detail_contentOverview
import com.example.worktrip.detail_contentTitle
import com.example.worktrip.detail_imgURL
import com.example.worktrip.detail_locationX
import com.example.worktrip.detail_locationY
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


private lateinit var binding : ActivityDetailCourseBinding
private lateinit var recyclerView_detail_course: RecyclerView

var changeType = false
class DetailCourseActivity : AppCompatActivity(){
    private lateinit var adapter: RecyclerAdapter_detail_course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCourseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_course)

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_detail_course))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        var getContentTypeId = intent.getStringExtra("contentTypeId")
        var getContentId = intent.getStringExtra("contentId")


        //키 값
        val key = "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
        //AND(안드로이드)
        val mobileOS = "&MobileOS=AND"
        //서비스명 = 어플명
        val mobileApp = "&MobileApp=WorkTrip"
        //type (xml/json)
        val _type = "&_type=xml"
        //content id (intent)
        val contentId = "&contentId=" + getContentId
        //관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
        val contentTypeId = getContentTypeId
        //기본정보조회여부( Y,N )
        val defaultYN = "&defaultYN=Y"
        //원본, 썸네일대표 이미지, 이미지 공공누리유형정보 조회여부( Y,N )
        val firstImageYN = "&firstImageYN=Y"
        //지역코드, 시군구코드조회여부( Y,N )
        val areaCodeYN = "&areacodeYN=Y"
        //대,중,소분류코드조회여부( Y,N )
        val catCodeYN = "&catcodeYN=Y"
        //	주소, 상세주소조회여부( Y,N )
        val addinfoYN = "&addrinfoYN=Y"
        //좌표X, Y 조회여부( Y,N )
        val mapInfoYN = "&mapinfoYN=Y"
        //콘텐츠개요조회여부( Y,N )
        val overviewYN = "&overviewYN=Y"
        //페이지번호
        val pageNo = "&pageNo=1"

        //API 정보를 가지고 있는 주소
        val url_detailCommon1 =
            "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + key + mobileOS + mobileApp + _type + contentId + contentTypeId + defaultYN + firstImageYN + areaCodeYN + catCodeYN + addinfoYN + mapInfoYN + overviewYN + pageNo

        //쓰레드 생성
        val thread_detailCommon1 = Thread(NetworkThread_detailCommon1(url_detailCommon1))
        thread_detailCommon1.start() // 쓰레드 시작
        thread_detailCommon1.join() // 멀티 작업 안되게 하려면 start 후 join 입력

        //키 값
        //val key = "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
        //AND(안드로이드)
        //val mobileOS = "&MobileOS=AND"
        //서비스명 = 어플명
        //val mobileApp = "&MobileApp=WorkTrip"
        //관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
        //val contentTypeId = getContentTypeId
        //대분류코드
        val cat1="&cat1="+ detail_contentCat1
        //중분류코드
        val cat2="&cat2="+ detail_contentCat2
        //소분류코드
        val cat3="&cat3="+ detail_contentCat3
        //type (xml/json)
        //val _type = "&_type=xml"

        //API 정보를 가지고 있는 주소
        val url_categoryCode1 =
            "https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=" + key + mobileOS + mobileApp + _type + contentTypeId + cat1 + cat2 + cat3 + _type


        val thread_categoryCode1 = Thread(NetworkThread_categoryCode1(url_categoryCode1))
        thread_categoryCode1.start() // 쓰레드 시작
        thread_categoryCode1.join() // 멀티 작업 안되게 하려면 start 후 join 입력

        //view
        var titleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_course_title)
        var locationTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_course_location)
        var imgImageView: ImageView = findViewById<ImageView>(R.id.iv_activity_detail_course_mainImage)
        var overviewTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_course_overview)
        var keywordTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_course_keyword)


        titleTextView.text = detail_contentTitle
        locationTextView.text = detail_contentLocation
        //imgImageView.setImageBitmap(detail_bitmap)
        Glide.with(this).load(detail_imgURL).centerInside().into(imgImageView)
        if (!(detail_imgURL.equals("")))
        {
            findViewById<ImageView>(R.id.iv_activity_detail_course_nullImage).visibility= View.GONE
        }
        overviewTextView.text = detail_contentOverview
        keywordTextView.text=detail_contentKeyword

        //kakaoMap
        val mapView = MapView(this)
        binding.rlActivityDetailCourseMap.addView(mapView)

        if (detail_locationX.equals("0.0") || detail_locationY.equals("0.0"))
        {
            Toast.makeText(applicationContext, "주소 정보가 없습니다.", Toast.LENGTH_LONG).show()
        }
        else
        {
            val marker = MapPOIItem()
            marker.apply {
                itemName = detail_contentTitle   // 마커 이름
                mapPoint = MapPoint.mapPointWithGeoCoord(detail_locationY.toDouble(), detail_locationX.toDouble())  // 좌표
                markerType = MapPOIItem.MarkerType.CustomImage          // 마커 모양 (커스텀)
                customImageResourceId = R.drawable.icon_picker_blue_filled            // 커스텀 마커 이미지
                selectedMarkerType = MapPOIItem.MarkerType.CustomImage  // 클릭 시 마커 모양 (커스텀)
                customSelectedImageResourceId = R.drawable.icon_picker_pink_filled       // 클릭 시 커스텀 마커 이미지
                isCustomImageAutoscale = true      // 커스텀 마커 이미지 크기 자동 조정
                setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점

            }
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(detail_locationY.toDouble(), detail_locationX.toDouble()), true)
            mapView.setZoomLevel(2, true)
            mapView.addPOIItem(marker)


            /*data_list_ntl_d("1", "d", "dd", "ddd", "dddd", "ddddd", 1)
            data_list_ntl_d("2", "dfsd", "ddafds", "ddafsdd", "ddddd", "ddddafd", 0)
            data_list_ntl_d("1", "asdfd", "asdfdd", "dasdfdd", "dddasdfasdd", "dddafsdfasdfdd", 1)

            if (changeType)
            {

            }else if (!changeType)
            {

            }
            recyclerView_detail_course=findViewById(R.id.rv_activity_detail_course_list!!)as RecyclerView
            recyclerView_detail_course.layoutManager= LinearLayoutManager(this)
            recyclerView_detail_course.adapter=adapter*/


        }

    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_bookmark_share, menu)
        return true
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.it_toolbar_bs_bookmark -> {
                //북마크 버튼 눌렀을 때
                if (item.isChecked==false)
                {
                    item.isChecked=true
                    item.setIcon(R.drawable.icon_bookmark_black_filled)
                    Toast.makeText(applicationContext, "북마크 o", Toast.LENGTH_LONG).show()
                }
                else if (item.isChecked==true)
                {
                    item.isChecked=false
                    item.setIcon(R.drawable.icon_bookmark_black)
                    Toast.makeText(applicationContext, "북마크 x", Toast.LENGTH_LONG).show()

                }

                return super.onOptionsItemSelected(item)
            }

            R.id.it_toolbar_bs_share -> {
                //공유 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "공유 실행", Toast.LENGTH_LONG).show()
                val bottomSheet = BottomsheetShareUrl()
                bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    /*fun showList() {
        val talk = if (changeType) {
            data_list_ntl_d("1", "d", "dd", "ddd", "dddd", "ddddd", 0)
        } else {
            data_list_ntl_d("1", "d", "dd", "ddd", "dddd", "ddddd", 1)
        }

        adapter.addItem(talk)

        recyclerView_detail_course=findViewById(R.id.rv_activity_detail_course_list!!)as RecyclerView
        recyclerView_detail_course.layoutManager= LinearLayoutManager(this)
        recyclerView_detail_course.adapter=adapter

        adapter.notifyItemChanged(adapter.list.size-1)
        changeType = !changeType
    }*/
}