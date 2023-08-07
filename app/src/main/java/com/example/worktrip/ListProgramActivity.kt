package com.example.worktrip

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.databinding.ActivityListProgramBinding
import com.example.worktrip.databinding.ActivityListRecommendedBinding
import com.google.android.material.chip.Chip


private lateinit var binding : ActivityListProgramBinding
private val list_card_list_people: ArrayList<data_card_list_people> = ArrayList()
private lateinit var recyclerView_list_people: RecyclerView

private lateinit var contentId: String

private lateinit var categoryArray: List<String>
private var searchCheck="program"

class ListProgramActivity: AppCompatActivity() {
    private var adapter = RecyclerAdapter_card_list_people(list_card_list_people)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProgramBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_list_program)

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_list_program))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = view.findViewById<TextView>(R.id.tv_activity_list_program_title)
        toolbarTitle.text = "프로그램"

        //칩 추가
        categoryArray= listOf("게임", "??", "??")
        CategoryChips(categoryArray)
        //

        //recyclerView
        list_card_list_people.add(data_card_list_people(null, "무슨 게임 1", "2명~4명", "1"))
        list_card_list_people.add(data_card_list_people(null, "무슨 게임 2", "2명", "2"))
        list_card_list_people.add(data_card_list_people(null, "무슨 게임 3", "2명", "3"))

        recyclerView_list_people=findViewById(R.id.rv_activity_list_program_list!!)as RecyclerView
        recyclerView_list_people.layoutManager= GridLayoutManager(this, 2)
        recyclerView_list_people.adapter=adapter


        intent = Intent(this, DetailProgramActivity::class.java)

        adapter.setOnClickListener( object : RecyclerAdapter_card_list_people.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                intent.putExtra("contentId", list_card_list_people[position].id)

                startActivity(intent)
                contentId=""
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        //초기화
        list_card_list_people.clear()
    }


    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search, menu)
        return true
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.it_toolbar_s_search -> {
                //검색 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                intent = Intent(this, HomeSearchActivity::class.java)
                intent.putExtra("searchCheck", searchCheck)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    //칩 추가
    fun CategoryChips(list:List<String>)
    {

        categoryArray= list
        for (i in 0..list.size-1)
        {
            val chip = Chip(this).apply {
                text = categoryArray[i]
                isCheckable = true
                isCheckedIconVisible = false
                chipStrokeWidth = 4f
                setTextSize(TypedValue.COMPLEX_UNIT_DIP , 16f)

                //테두리
                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                    intArrayOf(Color.rgb(39, 87, 255), Color.TRANSPARENT) //main_blue, transparent
                )

                //백그라운드
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                    intArrayOf(Color.WHITE, Color.rgb(39, 87, 255)) //white, main_blue
                )

                //텍스트
                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                        intArrayOf(Color.rgb(39, 87, 255), Color.WHITE) //main_blue, white
                    )

                )
            }
            binding.cpsActivityListProgram.addView(chip)
        }
    }
}