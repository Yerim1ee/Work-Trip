package com.example.worktrip.Home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worktrip.BottomsheetShareUrl
import com.example.worktrip.My.bookmarkImg
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityDetailProgramBinding
import com.example.worktrip.detail_contentLocation
import com.example.worktrip.detail_contentOverview
import com.example.worktrip.detail_contentTitle
import com.example.worktrip.detail_imgURL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

private lateinit var binding : ActivityDetailProgramBinding

private lateinit var recyclerView_list_num_text: RecyclerView
private val list_num_text: ArrayList<data_list_num_text> = ArrayList()
private val firestore_program= Firebase.firestore


private var dbContentTypeID=""
private var dbContentId=""
private var dbContentTitle=""
private var dbContentOverview=""
private var dbContentPeople=""
private var dbContentImage=""

private var isSaved=false
class DetailProgramActivity : AppCompatActivity() {
    private var adapter= RecyclerAdapter_list_num_text(list_num_text)
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProgramBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_detail_program))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        var getContentId = intent.getStringExtra("contentId").toString()
        KakaoSdk.init(this, resources.getString(R.string.kakao_app_key))


        //view
        var titleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_title)
        var peopleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_people)
        var timeTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_time)
        var preparationTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_preparation)
        var imgImageView: ImageView = findViewById<ImageView>(R.id.iv_activity_detail_program_mainImage)
        var keywordTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_keyword)
        var overviewTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_overview)
        var tipTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_tip)

        //기본 정보
        var programKeyword = ""
        var programTitle = ""
        var programImg = ""
        var programPeople = ""
        var programOverview = ""
        var programTime = ""
        var programTip = ""
        var programPreparation = ""

        firestore_program.collection("category_program").get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    if (document.data["id"].toString().equals(getContentId)) {

                        programKeyword = document.data["keyword"].toString() //필드 데이터
                        programTitle = document.data["title"].toString() //필드 데이터
                        programImg = document.data["image"].toString() //필드 데이터
                        programPeople = document.data["people"].toString() //필드 데이터
                        programOverview = document.data["overview"].toString() //필드 데이터
                        programTime = document.data["time"].toString() //필드 데이터
                        programTip = document.data["tip"].toString() //필드 데이터
                        programPreparation = document.data["preparation"].toString() //필드 데이터

                        titleTextView.text = programTitle
                        peopleTextView.text = programPeople
                        //imgImageView.setImageBitmap(bitmap)
                        Glide.with(this).load(programImg).centerInside().into(imgImageView)
                        if (!(programImg.equals("")) || !(programImg.equals("null"))) {
                            findViewById<ImageView>(R.id.iv_activity_detail_program_nullImage).visibility =
                                View.GONE
                        }
                        timeTextView.text = programTime
                        keywordTextView.text = programKeyword
                        overviewTextView.text = programOverview
                        preparationTextView.text = programPreparation
                        tipTextView.text = programTip

                        //북마크를 위한
                        dbContentTypeID = "program"
                        dbContentTitle = programTitle
                        dbContentOverview = programOverview
                        dbContentPeople = programPeople
                        dbContentId = getContentId
                        dbContentImage = programImg

                        break
                    }
                }

                //방법
                firestore_program.collection("category_program")
                    .document(getContentId).collection("how").get()
                    .addOnSuccessListener { task ->
                        for (document in task) {
                            var i = 1
                            while (true) {
                                var how = document.data[i.toString()].toString() //필드 데이터

                                if (how.equals("null")) {
                                    break
                                } else {
                                    list_num_text.add(data_list_num_text(i.toString(), how))
                                    i++
                                }
                            }

                        }
                        //recycler view
                        recyclerView_list_num_text =
                            view.findViewById(R.id.rv_activity_detail_program_list!!) as RecyclerView
                        recyclerView_list_num_text.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        recyclerView_list_num_text.adapter = adapter
                    }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        list_num_text.clear()
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
                    "contentLocation" to dbContentPeople,
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
                Button(
                    "자세히 보기",
                    Link(
                        //webUrl = "https://developers.kakao.com",
                        //mobileWebUrl = "https://developers.kakao.com"
                        androidExecutionParams = mapOf(
                            "contentType" to "program",
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
                    // Log.e(TAG, "카카오톡 공유 실패", error)
                }
                else if (sharingResult != null) {
                    // Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    // Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                    // Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
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