package com.worktrip.Home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.worktrip.My.firestore_bookmark_list
import com.worktrip.NetworkThread_categoryCode1
import com.worktrip.NetworkThread_detailCommon1
import com.worktrip.NetworkThread_detailInfo1
import com.worktrip.R
import com.worktrip.databinding.ActivityDetailCourseBinding
//import com.example.worktrip.detail_bitmap
import com.worktrip.detail_contentCat1
import com.worktrip.detail_contentCat2
import com.worktrip.detail_contentCat3
import com.worktrip.detail_contentKeyword
import com.worktrip.detail_contentLocation
import com.worktrip.detail_contentOverview
import com.worktrip.detail_contentTitle
import com.worktrip.detail_imgURL
import com.worktrip.detail_locationX
import com.worktrip.detail_locationY
import com.worktrip.detail_subcontentId
import com.worktrip.list_num_title_overview
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


private lateinit var binding : ActivityDetailCourseBinding

//private lateinit var recyclerView_detail_course: RecyclerView
private lateinit var recyclerView_detail_course: RecyclerView

private var dbContentTypeID=""
private var dbContentId=""
private var dbContentTitle=""
private var dbContentOverview=""
private var dbContentLocation=""
private var dbContentImage=""

//var changeType = false

private var isSaved=false

class DetailCourseActivity : AppCompatActivity(){
    //private var adapter= RecyclerAdapter_detail_course()
    private var adapter= RecyclerAdapter_list_num_title_overview(list_num_title_overview)
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCourseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_course)
        mAuth =FirebaseAuth.getInstance()

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_detail_course))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        //var getContentTypeId = intent.getStringExtra("contentTypeId")
        var getContentId = intent.getStringExtra("contentId")

        KakaoSdk.init(this, resources.getString(R.string.kakao_app_key))

        //키 값
        var key = "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
        //AND(안드로이드)
        var mobileOS = "&MobileOS=AND"
        //서비스명 = 어플명
        var mobileApp = "&MobileApp=WorkTrip"
        //type (xml/json)
        var _type = "&_type=xml"
        //content id (intent)
        var contentId = "&contentId=" + getContentId
        //관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
        var contentTypeId = "&contentTypeId=25"
        //기본정보조회여부( Y,N )
        var defaultYN = "&defaultYN=Y"
        //원본, 썸네일대표 이미지, 이미지 공공누리유형정보 조회여부( Y,N )
        var firstImageYN = "&firstImageYN=Y"
        //지역코드, 시군구코드조회여부( Y,N )
        var areaCodeYN = "&areacodeYN=Y"
        //대,중,소분류코드조회여부( Y,N )
        var catCodeYN = "&catcodeYN=Y"
        //	주소, 상세주소조회여부( Y,N )
        var addinfoYN = "&addrinfoYN=Y"
        //좌표X, Y 조회여부( Y,N )
        var mapInfoYN = "&mapinfoYN=Y"
        //콘텐츠개요조회여부( Y,N )
        var overviewYN = "&overviewYN=Y"
        //페이지번호
        var pageNo = "&pageNo=1"

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
        // Glide.with(this).load(detail_imgURL).centerInside().into(imgImageView)
        imgImageView.load(detail_imgURL)

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

        }

        if (list_num_title_overview.size!=0)
        {
            list_num_title_overview.clear()
        }

        //키 값
        //val key = "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
        //AND(안드로이드)
        //val mobileOS = "&MobileOS=AND"
        //서비스명 = 어플명
        //val mobileApp = "&MobileApp=WorkTrip"
        //type (xml/json)
        //val _type = "&_type=xml"
        //content id (intent)
        //val contentId = "&contentId=" + getContentId
        //관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
        //val contentTypeId = "&contentTypeId=25"

        //API 정보를 가지고 있는 주소
        val url_detailInfo1 = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=" + key + mobileOS + mobileApp + _type + contentId + contentTypeId

        //쓰레드 생성
        val thread_detailInfo1 = Thread(NetworkThread_detailInfo1(url_detailInfo1))
        thread_detailInfo1.start() // 쓰레드 시작
        thread_detailInfo1.join() // 멀티 작업 안되게 하려면 start 후 join 입력

        //recycler view
        recyclerView_detail_course=findViewById(R.id.rv_activity_detail_course_list!!)as RecyclerView
        recyclerView_detail_course.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_detail_course.adapter=adapter

        intent = Intent(this, DetailSightActivity::class.java)

        adapter.setOnClickListener( object : RecyclerAdapter_list_num_title_overview.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                var isSubcontent="true"

                //intent.putExtra("contentTypeId", contentTypeId)
                intent.putExtra("subcontentId", list_num_title_overview[position].subcontentId)
                intent.putExtra("subcontentTitle", list_num_title_overview[position].title)
                intent.putExtra("subcontentOverview", list_num_title_overview[position].overview)
                intent.putExtra("subcontentImg", list_num_title_overview[position].img)
                //intent.putExtra("subcontentId", list_num_title_overview[position].subcontentId)
                intent.putExtra("isSubcontent", isSubcontent)

                startActivity(intent)
                detail_subcontentId =""
                //contentTypeId=""
            }
        })

        //파이어베이스
        dbContentTypeID="&contentTypeId=39"
        dbContentTitle= detail_contentTitle
        dbContentOverview= detail_contentOverview
        dbContentLocation= detail_contentLocation
        dbContentId=getContentId.toString()
        dbContentImage= detail_imgURL

    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_bookmark_share, menu)
        firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").get()
            .addOnSuccessListener { task ->
                for (document in task) {

                    var id = document.data["contentID"].toString() //필드 데이터
                    if (id.equals(dbContentId))
                    {
                        isSaved=true
                        break
                    } else { isSaved = false }
                }

                if (isSaved==true)
                {
                    if (menu != null) {
                        var bookmark=menu.getItem(0)
                        if (bookmark!=null)
                        {
                            bookmark.setIcon(R.drawable.icon_bookmark_black_filled)
                        }
                    }
                }
            }

        return true
    }



    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.it_toolbar_bs_bookmark -> {
                //북마크 버튼 눌렀을 때
                val data_bookmark_list= hashMapOf(
                    "contentID" to dbContentId,
                    "contentTitle" to dbContentTitle,
                    "contentLocation" to dbContentLocation,
                    "contentOverview" to dbContentOverview,
                    "contentImage" to dbContentImage,
                    "contentTypeID" to dbContentTypeID
                )

                if (isSaved==true)
                {
                    item.isChecked=true
                }


                if (item.isChecked==false||isSaved==false) //item.isChecked==false
                {
                    item.isChecked=true
                    item.setIcon(R.drawable.icon_bookmark_black_filled)

                    //정보를 파이어베이스에 저장
                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).set(data_bookmark_list)

                    isSaved=true
                    Toast.makeText(applicationContext, "해당 정보가 북마크에 추가되었습니다.", Toast.LENGTH_LONG).show()

                }

                else if (item.isChecked==true||isSaved==true) //item.isChecked==true
                {
                    item.isChecked=false
                    item.setIcon(R.drawable.icon_bookmark_black)

                    //firestore_bookmark.collection("user_bookmark").document(/*유저 id*/).collection("list").document(dbContentId).delete()
                    deleteBookmark()
                }

                return super.onOptionsItemSelected(item)
            }

            R.id.it_toolbar_bs_share -> {
                //공유 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "공유 실행", Toast.LENGTH_LONG).show()
                //val bottomSheet = BottomsheetShareUrl()
                //bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                //bottomSheet.show(supportFragmentManager, bottomSheet.tag)

                kakaoShere()

                return super.onOptionsItemSelected(item)
            }

            android.R.id.home -> {
                finish()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
    private fun deleteBookmark()
    {
        firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).delete()
            ?.addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "해당 정보의 북마크를 제거했습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        isSaved=false
    }

    /*fun showList() {
        val talk = if (!changeType) {
            data_list_ntl_d("1", "d", "dd", "ddd", "dddd", "ddddd", 0)
        } else {
            data_list_ntl_d("1", "d", "dd", "ddd", "dddd", "ddddd", 1)
        }

        recyclerView_detail_course=findViewById(R.id.rv_activity_detail_course_list!!)as RecyclerView
        recyclerView_detail_course.layoutManager= LinearLayoutManager(this)
        recyclerView_detail_course.adapter=adapter

        adapter.notifyItemChanged(adapter.list_ntl_d.size-1)
        changeType = !changeType

        adapter.addItem(talk)
    }*/

    fun kakaoShere()
    {
        val defaultFeed = FeedTemplate(
            content = Content(
                title = dbContentTitle,
                description = dbContentOverview,
                imageUrl = dbContentImage,
                link = Link(
                    //webUrl = "https://developers.kakao.com",
                    //mobileWebUrl = "https://developers.kakao.com" //playstore url
                )
            ),
            buttons = listOf(
                Button(
                    "자세히 보기",
                    Link(
                        //webUrl = "https://developers.kakao.com",
                        //mobileWebUrl = "https://developers.kakao.com"
                        androidExecutionParams = mapOf(
                            "contentType" to "course",
                            "contentID" to dbContentId
                        )

                    )
                    )
                )
            )

        // 카카오톡 설치여부 확인
        if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(this, defaultFeed) { sharingResult, error ->
                if (error != null) {
                    // 카카오톡 공유 실패
                }
                else if (sharingResult != null) {
                    // 카카오톡 공유 성공
                    startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    // Warning Msg: ${sharingResult.warningMsg}
                    // Argument Msg: ${sharingResult.argumentMsg}
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
            } catch(e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(this, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }

}