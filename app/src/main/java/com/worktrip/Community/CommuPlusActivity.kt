package com.worktrip.Community

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import com.worktrip.R
import com.worktrip.databinding.ActivityCommuPlusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.worktrip.My.TabAdapter_bookmark
import java.text.SimpleDateFormat


private lateinit var binding: ActivityCommuPlusBinding
private lateinit var viewPager_plus: ViewPager2

lateinit var cb_company: CheckBox
private var cb_company_isChecked="비공개"

var editWriting=""

var editWritingID=""

var editTitle=""
var editDepature=""
var editDestination=""
var editDate=""
var editCompany=""
var editImg1=""
var editImg2=""
var editImg3=""

var editPeriod=""
var editKeyword=""
var editPeople =""
var editMoney=""

var editContent=""

var editGoal=""

class CommuPlusActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    private var uri1: Uri? = null
    private var uri2: Uri? = null
    private var uri3: Uri? = null

    private lateinit var writingID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommuPlusBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_commu_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //글을 수정할 때
        editWriting=intent.getStringExtra("editWriting").toString()

        editWritingID=intent.getStringExtra("editWritingID").toString()
        editDepature=intent.getStringExtra("editDepature").toString()
        editDestination=intent.getStringExtra("editDestination").toString()
        editDate=intent.getStringExtra("editDate").toString()
        editCompany=intent.getStringExtra("editCompany").toString()
        editImg1=intent.getStringExtra("editImg1").toString()
        editImg2=intent.getStringExtra("editImg2").toString()
        editImg3=intent.getStringExtra("editImg3").toString()

        editPeriod=intent.getStringExtra("editPeriod").toString()
        editKeyword=intent.getStringExtra("editKeyword").toString()
        editPeople=intent.getStringExtra("editPeople").toString()

        editTitle=intent.getStringExtra("editTitle").toString()
        editMoney=intent.getStringExtra("editMoney").toString()
        editContent=intent.getStringExtra("editContent").toString()
        editGoal=intent.getStringExtra("editGoal").toString()


        /*if (!(editWritingID.equals(null))||!(editWritingID.equals("null"))||!(editWritingID.equals("")))
        {
            /*firestore_community.collection("community").get()
                .addOnSuccessListener { task ->
                    for (document in task) {
                        if (document.data["writingID"].toString().equals(editWritingID)) {
                            //데이터
                            editTitle = document.data["title"].toString() //필드 데이터
                            editDepature = document.data["depature"].toString() //필드 데이터
                            editDestination = document.data["destination"].toString() //필드 데이터
                            editgDate = document.data["date"].toString() //필드 데이터
                            editCompany = document.data["company"].toString() //필드 데이터
                            editImg1 = document.data["img1"].toString() //필드 데이터
                            editImg2 = document.data["img2"].toString() //필드 데이터
                            editImg3 = document.data["img3"].toString() //필드 데이터

                            editPeriod = document.data["period"].toString() //필드 데이터
                            editKeyword = document.data["keyword"].toString() //필드 데이터
                            editPeople = document.data["people"].toString() //필드 데이터
                            editMoney = document.data["money"].toString() //필드 데이터

                            editContent = document.data["content"].toString() //필드 데이터

                            editGoal = document.data["goal"].toString() //필드 데이터

                            break
                        }}
                }*/

            findViewById<EditText>(R.id.et_fragment_plus_2_title).setText(editTitle)
            findViewById<EditText>(R.id.et_fragment_plus_2_content).setText(editContent)
        }*/

        val button=findViewById<Button>(R.id.btn_activity_commu_plus)
        val buttonPrevious=findViewById<LinearLayout>(R.id.ll_activity_commu_plus_previous)
        val subTitle=findViewById<TextView>(R.id.tv_activity_commu_plus_subtitle)
        val c1=findViewById<ImageView>(R.id.iv_activity_commu_plus_1)
        val c2=findViewById<ImageView>(R.id.iv_activity_commu_plus_2)
        cb_company=findViewById(R.id.cb_activity_commu_plus_company)


        //뷰페이저
        viewPager_plus = findViewById<ViewPager2>(R.id.vp_activity_commu_plus)

        val viewPagerAdapter = TabAdapter_bookmark(this)

        // fragment add
        viewPagerAdapter.addFragment(fragment_plus_1())
        viewPagerAdapter.addFragment(fragment_plus_2())
        if (editWriting.equals("editWriting"))
        {
            cb_company.isChecked = !editCompany.equals("비공개")
        }

        cb_company.setOnClickListener {
            if (cb_company.isChecked)
            {
                firestore_community.collection("user").get()
                    .addOnSuccessListener { task ->
                        for (document in task) {

                            if (document.id.equals("${mAuth.currentUser?.uid.toString()}"))
                            {
                                cb_company_isChecked =
                                    document.data["company"].toString()

                                if (cb_company_isChecked.equals("null") || cb_company_isChecked.equals(""))
                                {
                                    cb_company_isChecked = "회사 정보 없음"
                                }
                            }
                        }
                    }
            }
            else if (!cb_company.isChecked)
            {
                cb_company_isChecked="비공개"
            }
        }

        // adapter 연결
        viewPager_plus?.adapter = viewPagerAdapter
        viewPager_plus?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        button.text="다음으로"
                        subTitle.text="기본 정보 작성"
                        buttonPrevious.visibility= View.GONE
                        c1.setBackgroundResource(R.drawable.circle_filled)
                        c2.setBackgroundResource(R.drawable.circle_gray)

                        button.setOnClickListener{
                            viewPager_plus.setCurrentItem(position+1, true)
                        }
                    }
                    1 -> {
                        button.text="완료"
                        buttonPrevious.visibility= View.VISIBLE
                        buttonPrevious.setOnClickListener {
                            viewPager_plus.setCurrentItem(position-1, true)
                        }
                        subTitle.text="상세 정보 작성"
                        c2.setBackgroundResource(R.drawable.circle_filled)
                        c1.setBackgroundResource(R.drawable.circle_gray)

                            button.setOnClickListener {

                                commuTitle=findViewById<EditText>(R.id.et_fragment_plus_2_title).text.toString()
                                commuContent = findViewById<EditText>(R.id.et_fragment_plus_2_content).text.toString()

                                if (commuImage1.equals(""))
                                {
                                    //Toast.makeText(applicationContext, "이미지를 입력해 주세요.", Toast.LENGTH_LONG).show()
                                    commuImage1="없음"
                                }
                                if (commuImage2.equals(""))
                                {
                                    //Toast.makeText(applicationContext, "이미지를 입력해 주세요.", Toast.LENGTH_LONG).show()
                                    commuImage2="없음"
                                }
                                if (commuImage3.equals(""))
                                {
                                    //Toast.makeText(applicationContext, "이미지를 입력해 주세요.", Toast.LENGTH_LONG).show()
                                    commuImage3="없음"
                                }

                                if(commuImage1.equals("없음") && commuImage2.equals("없음") && commuImage3.equals("없음")){
                                    Toast.makeText(this@CommuPlusActivity, "사진을 추가해주세요",Toast.LENGTH_LONG).show()
                                }
                                else if(commuTitle.equals("")){
                                    Toast.makeText(this@CommuPlusActivity, "제목을 추가해주세요",Toast.LENGTH_LONG).show()

                                }
                                else if(commuContent.equals("")){
                                    Toast.makeText(this@CommuPlusActivity, "내용을 입력해주세요",Toast.LENGTH_LONG).show()

                                }
                                else {
                                    // 오늘 날짜 받아오기
                                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    //val today = LocalDate.now()
                                    val todayDate_format =
                                        formatter.format(System.currentTimeMillis()) // 형식 지정

                                    writingID =
                                        firestore_community.collection("community").document().id
                                    if (editWriting.equals("editWriting")) {
                                        writingID = editWritingID
                                    }
                                    //정보를 파이어베이스에 저장
                                    val data_community = hashMapOf(
                                        "writingID" to writingID,
                                        "date" to todayDate_format,
                                        "company" to cb_company_isChecked,
                                        "depature" to depature,
                                        "destination" to destination,
                                        "people" to people,
                                        "period" to period,
                                        "goal" to goal,
                                        "keyword" to keyword,
                                        "money" to money,
                                        "img1" to commuImage1,
                                        "img2" to commuImage2,
                                        "img3" to commuImage3,
                                        "title" to commuTitle,
                                        "content" to commuContent,
                                        "userID" to "${mAuth.currentUser?.uid.toString()}"
                                    )
                                    uri1 = commuImage1.toUri()
                                    uri2 = commuImage2.toUri()
                                    uri3 = commuImage3.toUri()

                                    // 스토리지에 올리기
                                    uri1?.let { imageUpload(it, writingID + "_1") }
                                    uri2?.let { imageUpload(it, writingID + "_2") }
                                    uri3?.let { imageUpload(it, writingID + "_3") }



                                    firestore_community.collection("community").document(writingID)
                                        .set(data_community)
                                }

                            }
                    }
                }
            }
        })


    }

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_null, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }


    private fun imageUpload(uri: Uri, title:String) {
        // storage 인스턴스 생성
        val storage = Firebase.storage
        // storage 참조
        val storageRef = storage.getReference("community/${mAuth.uid}")
        // 파일 경로와 이름으로 참조 변수 생성
        val mountainsRef = storageRef.child("${title}.png")


        val uploadTask = mountainsRef.putFile(uri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // 파일 업로드 성공

            val intent_detail = Intent(this@CommuPlusActivity, DetailWritingActivity::class.java)

            intent_detail.putExtra("writingID", writingID)
            startActivity(intent_detail)
            finish()

        }.addOnFailureListener {e ->
            // 파일 업로드 실패
            if(!(uri.toString().equals("없음"))){
                Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}