package com.example.worktrip

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.databinding.ActivityDetailCourseBinding
import com.example.worktrip.databinding.ActivityListRecommendedBinding


private lateinit var binding : ActivityDetailCourseBinding


class DetailCourseActivity : AppCompatActivity(){

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


        var titleTextView: TextView=findViewById<TextView>(R.id.tv_activity_detail_course_title)


        var putTitle=intent.getStringExtra("title")
        titleTextView.text=putTitle
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
                //검색 버튼 눌렀을 때
                Toast.makeText(applicationContext, "북마크 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }

            R.id.it_toolbar_bs_share -> {
                //검색 버튼 눌렀을 때
                Toast.makeText(applicationContext, "공유 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }
}

