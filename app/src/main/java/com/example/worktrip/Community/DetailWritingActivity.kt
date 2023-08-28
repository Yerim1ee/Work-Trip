package com.example.worktrip.Community

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityDetailWritingBinding
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView

private lateinit var binding: ActivityDetailWritingBinding

private var dbWritingID = ""
private var dbWritingTitle = ""
private var dbWritingDepature = ""
private var dbWritingDestination = ""
private var dbWritingDate = ""
private var dbWritingCompany = ""
private var dbWritingImg1 = ""
private var dbWritingImg2 = ""
private var dbWritingImg3 = ""

private var dbWritingPeriod = ""
private var dbWritingKeyword = ""
private var dbWritingPeople = ""
private var dbWritingMoney = ""

private var dbWritingContent = ""

private var dbWritingGoal = ""


private var isSaved=false
var good=1
private var isGood=false

class DetailWritingActivity  : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWritingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()

        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_detail_writing))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        var getWritingId = intent.getStringExtra("writingID").toString()

        //view
        var titleTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_title)
        var depatureTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_depature)
        var destinationTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_destination)
        var dateTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_date)
        var companyTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_company)
        //var userTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_user)
        var img1ImageView = findViewById<RoundedImageView>(R.id.iv_activity_detail_writing_1)
        var img2ImageView = findViewById<RoundedImageView>(R.id.iv_activity_detail_writing_2)
        var img3ImageView = findViewById<RoundedImageView>(R.id.iv_activity_detail_writing_3)

        var periodTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_period)
        var keywordTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_keyword)
        var peopleTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_people)
        var moneyTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_money)

        var contentTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_content)

        var goalTextView = findViewById<TextView>(R.id.tv_activity_detail_writing_goal)

        var goodTextView=findViewById<TextView>(R.id.tv_activity_detail_writing_good)
        var goodCheckBox=findViewById<CheckBox>(R.id.cb_activity_detail_writing_good)
        var goodIcon=findViewById<ImageView>(R.id.iv_activity_detail_writing_good)



        //기본 정보
        var writingTitle = ""
        var writingDepature = ""
        var writingDestination = ""
        var writingDate = ""
        var writingCompany = ""
        var writingImg1 = ""
        var writingImg2 = ""
        var writingImg3 = ""

        var writingPeriod = ""
        var writingKeyword = ""
        var writingPeople = ""
        var writingMoney = ""

        var writingContent = ""

        var writingGoal = ""

        //좋아요
        var goodInt=0

        firestore_community.collection("community").get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    if (document.data["writingID"].toString().equals(getWritingId)) {
                        //좋아요
                        firestore_community.collection("community").document(getWritingId).collection("good").get()
                            .addOnSuccessListener { task ->
                                for (document in task) {
                                    goodInt+=1
                                    if (document.id.equals("${mAuth.currentUser?.uid.toString()}"))
                                    {
                                        goodCheckBox.isChecked=true
                                        goodIcon.setBackgroundResource(R.drawable.icon_commu_good)
                                    }
                                }
                                goodTextView.text="+"+goodInt.toString()

                                goodCheckBox.setOnClickListener {
                                    if (goodCheckBox.isChecked)
                                    {
                                        firestore_community.collection("community").document(getWritingId).collection("good").document("${mAuth.currentUser?.uid.toString()}").set("isGood" to true)
                                        goodInt+=1
                                        goodCheckBox.isChecked=true
                                        goodIcon.setBackgroundResource(R.drawable.icon_commu_good)
                                        goodTextView.text="+"+goodInt.toString()

                                    }
                                    else if (!(goodCheckBox.isChecked))
                                    {
                                        firestore_community.collection("community").document(getWritingId).collection("good").document("${mAuth.currentUser?.uid.toString()}").delete()
                                        goodInt-=1
                                        goodCheckBox.isChecked=false
                                        goodIcon.setBackgroundResource(R.drawable.icon_commu_good_line)
                                        goodTextView.text="+"+goodInt.toString()

                                    }
                                }


                            }

                        //데이터
                        writingTitle = document.data["title"].toString() //필드 데이터
                        writingDepature = document.data["depature"].toString() //필드 데이터
                        writingDestination = document.data["destination"].toString() //필드 데이터
                        writingDate = document.data["date"].toString() //필드 데이터
                        writingCompany = document.data["company"].toString() //필드 데이터
                        writingImg1 = document.data["img1"].toString() //필드 데이터
                        writingImg2 = document.data["img2"].toString() //필드 데이터
                        writingImg3 = document.data["img3"].toString() //필드 데이터

                        writingPeriod = document.data["period"].toString() //필드 데이터
                        writingKeyword = document.data["keyword"].toString() //필드 데이터
                        writingPeople = document.data["people"].toString() //필드 데이터
                        writingMoney = document.data["money"].toString() //필드 데이터

                        writingContent = document.data["content"].toString() //필드 데이터

                        writingGoal = document.data["goal"].toString() //필드 데이터

                        titleTextView.text = writingTitle
                        depatureTextView.text = writingDepature
                        destinationTextView.text = writingDestination
                        dateTextView.text = writingDate
                        companyTextView.text = writingCompany

                        if (!(writingImg1.equals("없음"))) {
                            findViewById<ImageView>(R.id.iv_activity_detail_writing_null1).visibility =
                                View.GONE
                            img1ImageView.visibility = View.VISIBLE
                            Glide.with(this).load(writingImg1).centerInside().into(img1ImageView)
                        }

                        if (!(writingImg2.equals("없음"))) {
                            img2ImageView.visibility = View.VISIBLE

                            Glide.with(this).load(writingImg2).centerInside().into(img2ImageView)
                        }


                        if (!(writingImg3.equals("없음"))) {
                            img3ImageView.visibility = View.VISIBLE

                            Glide.with(this).load(writingImg3).centerInside().into(img3ImageView)
                        }

                        periodTextView.text = writingPeriod
                        keywordTextView.text = writingKeyword
                        peopleTextView.text = writingPeople
                        moneyTextView.text = writingMoney

                        contentTextView.text = writingContent

                        goalTextView.text = writingGoal

                        //북마크를 위한
                        dbWritingID=getWritingId
                        dbWritingTitle=writingTitle
                        dbWritingDepature=writingDepature
                        dbWritingDestination=writingDestination
                        dbWritingDate=writingDate
                        dbWritingCompany=writingCompany

                        dbWritingImg1=writingImg1
                        dbWritingImg2=writingImg2
                        dbWritingImg3=writingImg3

                        dbWritingPeriod=writingPeriod
                        dbWritingKeyword=writingKeyword
                        dbWritingPeople=writingPeople
                        dbWritingMoney=writingMoney

                        dbWritingContent=writingContent

                        dbWritingGoal=writingGoal

                        break
                    }
                }

            }
    }

        //toolbar
        //툴바 메뉴 연결
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.toolbar_bookmark_edit, menu)
            firestore_bookmark_list.collection("user_bookmark")
                .document("${mAuth.currentUser?.uid.toString()}").collection("community").get()
                .addOnSuccessListener { task ->
                    for (document in task) {

                        var id = document.data["writingID"].toString() //필드 데이터
                        if (id.equals(dbWritingID)) {
                            isSaved = true
                            break
                        } else {
                            isSaved = false
                        }
                    }

                    if (isSaved == true) {
                        if (menu != null) {
                            var bookmark = menu.getItem(0)
                            if (bookmark != null) {
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
                R.id.it_toolbar_be_bookmark -> {
                    //북마크 버튼 눌렀을 때
                    val data_bookmark_community = hashMapOf(
                        "writingID" to dbWritingID,
                        "date" to dbWritingDate,
                        "company" to dbWritingCompany,
                        "depature" to dbWritingDepature,
                        "destination" to dbWritingDestination,
                        "people" to dbWritingPeople,
                        "period" to dbWritingPeriod,
                        "goal" to dbWritingGoal,
                        "keyword" to dbWritingKeyword,
                        "money" to dbWritingMoney,
                        "img1" to dbWritingImg1,
                        "img2" to dbWritingImg2,
                        "img3" to dbWritingImg3,
                        "title" to dbWritingTitle,
                        "content" to dbWritingContent,
                        "userID" to "${mAuth.currentUser?.uid.toString()}"
                    )

                    if (isSaved == true) {
                        item.isChecked = true
                    }


                    if (item.isChecked == false || isSaved == false) //item.isChecked==false
                    {
                        item.isChecked = true
                        item.setIcon(R.drawable.icon_bookmark_black_filled)

                        //정보를 파이어베이스에 저장
                        firestore_bookmark_list.collection("user_bookmark")
                            .document("${mAuth.currentUser?.uid.toString()}").collection("community")
                            .document(writingID).set(data_bookmark_community)

                        isSaved = true
                        Toast.makeText(
                            applicationContext,
                            "해당 정보가 북마크에 추가되었습니다.",
                            Toast.LENGTH_LONG
                        )
                            .show()

                    } else if (item.isChecked == true || isSaved == true) //item.isChecked==true
                    {
                        item.isChecked = false
                        item.setIcon(R.drawable.icon_bookmark_black)

                        //firestore_bookmark.collection("user_bookmark").document(/*유저 id*/).collection("list").document(dbContentId).delete()
                        deleteBookmark()
                    }

                    return super.onOptionsItemSelected(item)
                }

                R.id.it_toolbar_be_edit -> {
                    //수정하기 눌렀을 때

                }

                R.id.it_toolbar_be_delete -> {
                    //삭제하기 눌렀을 때

                }

                R.id.it_toolbar_be_report -> {
                    //신고하기 눌렀을 때

                }

                android.R.id.home -> {
                    finish()
                    return super.onOptionsItemSelected(item)
                }
            }

            return super.onOptionsItemSelected(item)

        }

        private fun deleteBookmark() {
            firestore_bookmark_list.collection("user_bookmark")
                .document("${mAuth.currentUser?.uid.toString()}").collection("community").document(dbWritingID).delete()
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
            isSaved = false
        }
}