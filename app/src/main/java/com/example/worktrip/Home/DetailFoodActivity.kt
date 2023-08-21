package com.example.worktrip.Home

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.worktrip.BottomsheetShareUrl
import com.example.worktrip.My.BookmarkActivity
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.NetworkThread_categoryCode1
import com.example.worktrip.NetworkThread_detailCommon1
import com.example.worktrip.NetworkThread_detailIntroFood
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityDetailFoodBinding
//import com.example.worktrip.detail_bitmap
import com.example.worktrip.detail_contentCat1
import com.example.worktrip.detail_contentCat2
import com.example.worktrip.detail_contentCat3
import com.example.worktrip.detail_contentClose
import com.example.worktrip.detail_contentFirstmenu
import com.example.worktrip.detail_contentKeyword
import com.example.worktrip.detail_contentLocation
import com.example.worktrip.detail_contentOpen
import com.example.worktrip.detail_contentOverview
import com.example.worktrip.detail_contentParkingFod
import com.example.worktrip.detail_contentReservationFod
import com.example.worktrip.detail_contentTel
import com.example.worktrip.detail_contentTitle
import com.example.worktrip.detail_contentTreatmenu
import com.example.worktrip.detail_imgURL
import com.example.worktrip.detail_locationX
import com.example.worktrip.detail_locationY
import com.example.worktrip.list_card_list
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.properties.Delegates

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

        //view
        var titleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_title)
        var locationTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_location)
        var imgImageView: ImageView = findViewById<ImageView>(R.id.iv_activity_detail_food_mainImage)
        var overviewTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_overview)
        var keywordTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_food_keyword)


        titleTextView.text = detail_contentTitle
        locationTextView.text = detail_contentLocation
        //imgImageView.setImageBitmap(detail_bitmap)
        Glide.with(this).load(detail_imgURL).centerInside().into(imgImageView)
        if (!(detail_imgURL.equals("")))
        {
            findViewById<ImageView>(R.id.iv_activity_detail_food_nullImage).visibility= View.GONE
        }
        overviewTextView.text = detail_contentOverview
        keywordTextView.text=detail_contentKeyword

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
                val bottomSheet = BottomsheetShareUrl()
                bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
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
}
