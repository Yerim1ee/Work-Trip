package com.example.worktrip.Home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.worktrip.BottomsheetShareUrl
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityDetailSightBinding
import com.example.worktrip.detail_contentKeyword
import com.example.worktrip.detail_contentLocation
import com.example.worktrip.detail_contentOverview
import com.example.worktrip.detail_contentTitle
import com.example.worktrip.detail_imgURL
import com.example.worktrip.detail_locationX
import com.example.worktrip.detail_locationY
import com.google.firebase.auth.FirebaseAuth
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

private lateinit var binding : ActivityDetailSightBinding

private var dbContentTypeID=""
private var dbContentId=""
private var dbContentTitle=""
private var dbContentOverview=""
private var dbContentLocation=""
private var dbContentImage=""

private var isSaved=false
private var getSubcontent="false"

class DetailSightActivity : AppCompatActivity(){
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSightBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_course)
        mAuth = FirebaseAuth.getInstance()

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_detail_sight))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //view
        var titleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_sight_title)
        var locationTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_sight_location)
        var imgImageView: ImageView = findViewById<ImageView>(R.id.iv_activity_detail_sight_mainImage)
        var overviewTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_sight_overview)
        var keywordTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_sight_keyword)

        getSubcontent = intent.getStringExtra("isSubcontent").toString()

        if (getSubcontent.equals("true"))
        {
            //intent
            var getSubcontentId = intent.getStringExtra("subcontentId")
            var getSubcontentTitle = intent.getStringExtra("subcontentTitle")
            var getSubcontentOverview = intent.getStringExtra("subcontentOverview")
            var getSubcontentImg = intent.getStringExtra("subcontentImg")

            titleTextView.text = getSubcontentTitle
            locationTextView.text = "주소 정보가 없습니다."
            //imgImageView.setImageBitmap(detail_bitmap)
            Glide.with(this).load(getSubcontentImg).centerInside().into(imgImageView)
            if (!(getSubcontentImg.equals("")))
            {
                findViewById<ImageView>(R.id.iv_activity_detail_sight_nullImage).visibility= View.GONE
            }
            overviewTextView.text = getSubcontentOverview
            keywordTextView.visibility=View.GONE

            Toast.makeText(applicationContext, "주소 정보가 없습니다.", Toast.LENGTH_LONG).show()

            binding.rlActivityDetailSightMap.visibility=View.GONE

            //파이어베이스
            /*
            dbContentTypeID ="&contentTypeId=39"
            dbContentTitle = detail_contentTitle
            dbContentOverview = detail_contentOverview
            dbContentLocation = detail_contentLocation
            dbContentId=getContentId.toString()
            dbContentImage = detail_imgURL*/
        }
        else
        {
            titleTextView.text = detail_contentTitle
            locationTextView.text = detail_contentLocation
            //imgImageView.setImageBitmap(detail_bitmap)
            Glide.with(this).load(detail_imgURL).centerInside().into(imgImageView)
            if (!(detail_imgURL.equals("")))
            {
                findViewById<ImageView>(R.id.iv_activity_detail_course_nullImage).visibility= View.GONE
            }
            overviewTextView.text = detail_contentOverview
            keywordTextView.text= detail_contentKeyword

            //kakaoMap
            val mapView = MapView(this)
            binding.rlActivityDetailSightMap.addView(mapView)

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

                //파이어베이스
                /*
                dbContentTypeID ="&contentTypeId=39"
                dbContentTitle = detail_contentTitle
                dbContentOverview = detail_contentOverview
                dbContentLocation = detail_contentLocation
                dbContentId=getContentId.toString()
                dbContentImage = detail_imgURL*/

            }
        }


    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (getSubcontent.equals("true"))
        {
            menuInflater.inflate(R.menu.toolbar_null, menu)
        }
        else if (getSubcontent.equals("false"))
        {
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
        }

        return true
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (getSubcontent.equals("true"))
        {
            when (item.itemId) {
                android.R.id.home -> finish()
            }
        }
        else if (getSubcontent.equals("false"))
        {
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
                        firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(
                            dbContentId
                        ).set(data_bookmark_list)

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
                }

                R.id.it_toolbar_bs_share -> {
                    //공유 버튼 눌렀을 때
                    //Toast.makeText(applicationContext, "공유 실행", Toast.LENGTH_LONG).show()
                    val bottomSheet = BottomsheetShareUrl()
                    bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                    return super.onOptionsItemSelected(item)
                }

                android.R.id.home -> {
                    finish()
                    return super.onOptionsItemSelected(item)
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }
    private fun deleteBookmark()
    {
        firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(
            dbContentId
        ).delete()
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