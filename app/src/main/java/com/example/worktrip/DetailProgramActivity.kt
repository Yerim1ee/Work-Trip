package com.example.worktrip

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.databinding.ActivityDetailProgramBinding

private lateinit var binding : ActivityDetailProgramBinding

private var contentTitle: String=""
private var contentPeople: String=""
private var contentTime: String=""
private var contentKeyword: String=""
private lateinit var bitmap: Bitmap
private var contentOverview: String=""


class DetailProgramActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProgramBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_detail_program))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        var getContentId = intent.getStringExtra("contentId")

        //view
        var titleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_title)
        var peopleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_people)
        var overviewPeopleTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_overviewPeople)
        var timeTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_overviewTime)
        //var imgImageView: ImageView = findViewById<ImageView>(R.id.iv_activity_detail_program_mainImage)
        var keywordTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_keyword)
        var overviewTextView: TextView = findViewById<TextView>(R.id.tv_activity_detail_program_overview)

        if (getContentId.equals("1"))
        {
            contentTitle="무슨 게임 1"
            contentPeople="2명~4명"
            contentTime="15분"
            contentKeyword="게임"

            contentOverview="2명~4명2명~4명2명~4명2명~4명2명~4명2명~4명2명~4명2명~4명2명~4명2명~4명"
        }
        else if(getContentId.equals("2"))
        {
            contentTitle="무슨 게임 2"
            contentPeople="2명"
            contentTime="155분"
            contentKeyword="게임"

            contentOverview="155분155분155분155분155분155분155분155분155분155분"
        }
        else
        {
            contentTitle="무슨 게임 3"
            contentPeople="2명"
            contentTime="152225분"
            contentKeyword="게임"

            contentOverview="무슨 게임 3무슨 게임 3무슨 게임 3무슨 게임 3무슨 게임 3무슨 게임 3무슨 게임 3"
        }

        titleTextView.text = contentTitle
        peopleTextView.text = contentPeople
        //imgImageView.setImageBitmap(bitmap)
        overviewPeopleTextView.text = contentPeople
        timeTextView.text = contentTime
        keywordTextView.text = contentKeyword
        overviewTextView.text = contentOverview

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
                Toast.makeText(applicationContext, "공유 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }
}