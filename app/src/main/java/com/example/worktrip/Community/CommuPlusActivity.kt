package com.example.worktrip.Community

import android.content.Intent
import android.graphics.drawable.Drawable
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
import androidx.viewpager2.widget.ViewPager2
import com.example.worktrip.Home.DetailFoodActivity
import com.example.worktrip.My.TabAdapter_bookmark
import com.example.worktrip.My.bookmarkImg
import com.example.worktrip.My.fragment_bookmark_commu
import com.example.worktrip.My.fragment_bookmark_list
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityCommuPlusBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private lateinit var binding: ActivityCommuPlusBinding
private lateinit var viewPager_plus: ViewPager2

lateinit var cb_company: CheckBox
private var cb_company_isChecked=""

var writingID=""
class CommuPlusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommuPlusBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val mAuth = FirebaseAuth.getInstance()

        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_commu_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼


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

                                if (commuTitle.equals(""))
                                {
                                    //button.setBackgroundResource(R.drawable.chips_keyword3)
                                    //Toast.makeText(applicationContext, "제목을 입력해 주세요.", Toast.LENGTH_LONG).show()
                                    commuTitle="제목 없음"
                                }
                                if (commuContent.equals(""))
                                {
                                    //Toast.makeText(applicationContext, "내용을 입력해 주세요.", Toast.LENGTH_LONG).show()
                                    commuContent="내용 없음"
                                }

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

                                // 오늘 날짜 받아오기
                                val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                                val today = LocalDate.now()
                                val todayDate_format = today.format(formatter) // 형식 지정

                                //정보를 파이어베이스에 저장
                                writingID=firestore_community.collection("community").document().id
                                val data_community= hashMapOf(
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
                                firestore_community.collection("community").document(writingID)
                                    .set(data_community)

                                val intent = Intent(this@CommuPlusActivity, DetailWritingActivity::class.java)

                                intent.putExtra("writingID", writingID)
                                startActivity(intent)
                                //finish()

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
}