package com.worktrip.Home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.worktrip.My.firestore_bookmark_list
import com.worktrip.NetworkThread_categoryCode1
import com.worktrip.NetworkThread_detailCommon1
import com.worktrip.NetworkThread_detailIntroFood
import com.worktrip.R
import com.worktrip.databinding.ActivityDetailFoodBinding
//import com.example.worktrip.detail_bitmap
import com.worktrip.detail_contentCat1
import com.worktrip.detail_contentCat2
import com.worktrip.detail_contentCat3
import com.worktrip.detail_contentClose
import com.worktrip.detail_contentFirstmenu
import com.worktrip.detail_contentKeyword
import com.worktrip.detail_contentLocation
import com.worktrip.detail_contentOpen
import com.worktrip.detail_contentOverview
import com.worktrip.detail_contentParkingFod
import com.worktrip.detail_contentReservationFod
import com.worktrip.detail_contentTel
import com.worktrip.detail_contentTitle
import com.worktrip.detail_contentTreatmenu
import com.worktrip.detail_imgURL
import com.worktrip.detail_locationX
import com.worktrip.detail_locationY
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

private lateinit var binding : ActivityDetailFoodBinding

private var dbContentTypeID=""
private var dbContentId=""
private var dbContentTitle=""
private var dbContentOverview=""
private var dbContentLocation=""
private var dbContentImage=""

private var isSaved=false

class DetailFoodActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_food)
        mAuth =FirebaseAuth.getInstance()

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_detail_food))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        //var getContentTypeId = intent.getStringExtra("contentTypeId")
        var getContentId = intent.getStringExtra("contentId")
        KakaoSdk.init(this, resources.getString(R.string.kakao_app_key))

        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null) {
                //여기서 androidExecutionParams 값들을 받아와 어떠한 상세페이지를 띄울지 결정할 수 있음
                val contentType = uri.getQueryParameter("contentType")
                val contentID = uri.getQueryParameter("contentID")

                val bundle = Bundle()
                if (contentID != null) {
                    bundle.putInt("contentID", contentID.toInt())
                    //togetherFragment.arguments = bundle
                    getContentId=contentID.toString()
                    if (contentType.equals("course"))
                    {
                        intent = Intent(this, DetailCourseActivity::class.java)
                        intent.putExtra("contentId", getContentId)
                        startActivity(intent)
                    }
                    else if (contentType.equals("lodging"))
                    {
                        intent = Intent(this, DetailLodgingActivity::class.java)
                        intent.putExtra("contentId", getContentId)
                        startActivity(intent)
                    }
                    else if (contentType.equals("program"))
                    {
                        intent = Intent(this, DetailProgramActivity::class.java)
                        intent.putExtra("contentId", getContentId)
                        startActivity(intent)
                    }
                    else if (contentType.equals("food"))
                    {
                        //intent = Intent(this, DetailFoodActivity::class.java)
                        //intent.putExtra("contentId", getContentId)
                        //startActivity(intent)
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
                        val contentTypeId = "&contentTypeId=39"
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
                        //val contentTypeId = getContentTypeId


                        //API 정보를 가지고 있는 주소
                        val url_detailIntroFood =
                            "https://apis.data.go.kr/B551011/KorService1/detailIntro1?serviceKey=" + key + mobileOS + mobileApp + _type + contentId + contentTypeId


                        val thread_detailIntroFood = Thread(NetworkThread_detailIntroFood(url_detailIntroFood))
                        thread_detailIntroFood.start() // 쓰레드 시작
                        thread_detailIntroFood.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        //kakaoMap
                        val mapView = MapView(this)
                        binding.rlActivityDetailFoodMap.addView(mapView)

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
                    }
                }
            }
        }

        else
        {
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
            val contentTypeId = "&contentTypeId=39"
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
            //val contentTypeId = getContentTypeId


            //API 정보를 가지고 있는 주소
            val url_detailIntroFood =
                "https://apis.data.go.kr/B551011/KorService1/detailIntro1?serviceKey=" + key + mobileOS + mobileApp + _type + contentId + contentTypeId


            val thread_detailIntroFood = Thread(NetworkThread_detailIntroFood(url_detailIntroFood))
            thread_detailIntroFood.start() // 쓰레드 시작
            thread_detailIntroFood.join() // 멀티 작업 안되게 하려면 start 후 join 입력

            //kakaoMap
            val mapView = MapView(this)
            binding.rlActivityDetailFoodMap.addView(mapView)

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
        }

        //view
        var titleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_title)
        var locationTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_location)
        var imgImageView: ImageView = findViewById<ImageView>(R.id.iv_activity_detail_food_mainImage)
        var overviewTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_overview)
        var keywordTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_keyword)


        titleTextView.text = detail_contentTitle
        locationTextView.text = detail_contentLocation
        //imgImageView.setImageBitmap(detail_bitmap)
        // Glide.with(this).load(detail_imgURL).centerInside().into(imgImageView)
        imgImageView.load(detail_imgURL)

        if (!(detail_imgURL.equals("")))
        {
            findViewById<ImageView>(R.id.iv_activity_detail_food_nullImage).visibility= View.GONE
        }
        overviewTextView.text = detail_contentOverview
        keywordTextView.text=detail_contentKeyword


        //상세정보
        var firstMenuTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_firstMenu)
        var treatMenuTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_treatMenu)

        var openTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_open)
        var closeTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_close)
        var parkingTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_parking)
        var telTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_tel)
        var reservationTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_reservation)

        firstMenuTextView.text=detail_contentFirstmenu
        treatMenuTextView.text=detail_contentTreatmenu

        openTextView.text=detail_contentOpen
        closeTextView.text=detail_contentClose
        parkingTextView.text=detail_contentParkingFod
        telTextView.text=detail_contentTel
        reservationTextView.text=detail_contentReservationFod

        //하단 버튼
        var moreButton: TextView = findViewById<Button>(R.id.btn_activity_detail_food_more)

        if (detail_contentTel.equals("전화번호 정보 없음"))
        {
            moreButton.setBackgroundResource(R.drawable.chips_keyword3)
            moreButton.setText("전화번호가 없습니다")
        }
        else
        {
            moreButton.setOnClickListener{

                var intent_detail_contentTel=detail_contentTel.replace("-","")
                //Toast.makeText(applicationContext, intent_detail_contentTel, Toast.LENGTH_LONG).show()
                var intentMore = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+intent_detail_contentTel))
                startActivity(intentMore)
            }
        }
        dbContentTypeID="&contentTypeId=39"
        dbContentTitle= detail_contentTitle
        dbContentOverview= detail_contentOverview
        dbContentLocation= detail_contentLocation
        dbContentId=getContentId.toString()
        dbContentImage= detail_imgURL
    }

    override fun onDestroy() {
        super.onDestroy()
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
                com.kakao.sdk.template.model.Button(
                    "자세히 보기",
                    Link(
                        //webUrl = "https://developers.kakao.com",
                        //mobileWebUrl = "https://developers.kakao.com"
                        androidExecutionParams = mapOf(
                            "contentType" to "food",
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
