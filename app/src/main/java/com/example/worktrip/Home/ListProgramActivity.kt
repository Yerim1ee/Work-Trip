package com.example.worktrip.Home

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.Home.DetailProgramActivity
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.NetworkThread_list
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityListProgramBinding
import com.example.worktrip.list_card_list
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth


private lateinit var binding : ActivityListProgramBinding
private val list_card_list_people: ArrayList<data_card_list_people> = ArrayList()
private lateinit var recyclerView_list_people: RecyclerView

//private lateinit var contentId: String

private lateinit var categoryArray: List<String>
private var searchCheck="program"

private var programId=""
private var programKeyword=""
private var programTitle=""
private var programImg=""
private var programPeople=""
private var programOverview=""


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
        categoryArray = listOf("아이스브레이킹", "단합", "레크레이션")
        CategoryChips(categoryArray)
        //

        //recyclerView
        firestore_bookmark_list.collection("category_program")
            .get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    programId = document.data["id"].toString() //필드 데이터
                    programKeyword = document.data["keyword"].toString() //필드 데이터
                    programTitle = document.data["title"].toString() //필드 데이터
                    programImg = document.data["image"].toString() //필드 데이터
                    programPeople = document.data["people"].toString() //필드 데이터
                    programOverview = document.data["overview"].toString() //필드 데이터

                    list_card_list_people.add(
                        data_card_list_people(
                            programId,
                            programKeyword,
                            programTitle,
                            programImg,
                            programPeople,
                            programOverview
                        )
                    )
                }
                //샘플 데이터 삭제
                list_card_list_people.removeLast()

                //recycler view
                recyclerView_list_people =
                    findViewById(R.id.rv_activity_list_program_list!!) as RecyclerView
                recyclerView_list_people.layoutManager = GridLayoutManager(this, 2)
                recyclerView_list_people.adapter = adapter


                var intent = Intent(this, DetailProgramActivity::class.java)

                adapter.setOnClickListener(object :
                    RecyclerAdapter_card_list_people.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                        adapter.setOnClickListener(object :
                            RecyclerAdapter_card_list_people.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                                intent.putExtra("contentId", list_card_list_people[position].id)

                                startActivity(intent)
                                //contentId = ""
                            }
                        })
                    }
                })


            }
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

        categoryArray = list
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

            chip.setOnClickListener{
                if(chip.isChecked)
                {
                    //Toast.makeText(applicationContext, "${chip.id}번째 칩", Toast.LENGTH_LONG).show()
                    var keyword=""
                    if (chip.id==1) //아이스브레이킹
                    {
                        //Toast.makeText(applicationContext, "${chip.id}번째 칩-프로그램", Toast.LENGTH_LONG).show()
                        list_card_list_people.clear()
                        keyword="아이스브레이킹"
                        keywordList(keyword)
                    }
                    else if (chip.id==2) //단합
                    {
                        //Toast.makeText(applicationContext, "${chip.id}번째 칩-프로그램", Toast.LENGTH_LONG).show()

                        list_card_list_people.clear()
                        keyword="단합"
                        keywordList(keyword)
                    }
                    else if (chip.id==3) //레크레이션
                    {
                        //Toast.makeText(applicationContext, "${chip.id}번째 칩-프로그램", Toast.LENGTH_LONG).show()

                        list_card_list_people.clear()
                        keyword="레크레이션"
                        keywordList(keyword)
                    }

                }

                else if(!chip.isChecked)
                {
                    list_card_list_people.clear()
                    //recyclerView
                    firestore_bookmark_list.collection("category_program")
                        .get()
                        .addOnSuccessListener { task ->
                            for (document in task) {
                                programId = document.data["id"].toString() //필드 데이터
                                programKeyword = document.data["keyword"].toString() //필드 데이터
                                programTitle = document.data["title"].toString() //필드 데이터
                                programImg = document.data["image"].toString() //필드 데이터
                                programPeople = document.data["people"].toString() //필드 데이터
                                programOverview = document.data["overview"].toString() //필드 데이터

                                list_card_list_people.add(
                                    data_card_list_people(
                                        programId,
                                        programKeyword,
                                        programTitle,
                                        programImg,
                                        programPeople,
                                        programOverview
                                    )
                                )
                            }
                            //샘플 데이터 삭제
                            list_card_list_people.removeLast()

                            //recycler view
                            recyclerView_list_people =
                                findViewById(R.id.rv_activity_list_program_list!!) as RecyclerView
                            recyclerView_list_people.layoutManager = GridLayoutManager(this, 2)
                            recyclerView_list_people.adapter = adapter


                            var intent = Intent(this, DetailProgramActivity::class.java)

                            adapter.setOnClickListener(object :
                                RecyclerAdapter_card_list_people.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                                    adapter.setOnClickListener(object :
                                        RecyclerAdapter_card_list_people.ItemClickListener {
                                        override fun onClick(view: View, position: Int) {
                                            //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                                            intent.putExtra("contentId", list_card_list_people[position].id)

                                            startActivity(intent)
                                            //contentId = ""
                                        }
                                    })
                                }
                            })

                        }
                }
            }

        }
    }

    private fun keywordList(keyword: String)
    {
        //recyclerView
        firestore_bookmark_list.collection("category_program")
            .get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    if (document.data["keyword"].toString().equals(keyword)) {
                        programId = document.data["id"].toString() //필드 데이터
                        programKeyword = document.data["keyword"].toString() //필드 데이터
                        programTitle = document.data["title"].toString() //필드 데이터
                        programImg = document.data["image"].toString() //필드 데이터
                        programPeople = document.data["people"].toString() //필드 데이터
                        programOverview = document.data["overview"].toString() //필드 데이터

                        list_card_list_people.add(
                            data_card_list_people(
                                programId,
                                programKeyword,
                                programTitle,
                                programImg,
                                programPeople,
                                programOverview
                            )
                        )
                    }
                }
                //recycler view
                recyclerView_list_people =
                    findViewById(R.id.rv_activity_list_program_list!!) as RecyclerView
                recyclerView_list_people.layoutManager = GridLayoutManager(this, 2)
                recyclerView_list_people.adapter = adapter


                var intent = Intent(this, DetailProgramActivity::class.java)

                adapter.setOnClickListener(object :
                    RecyclerAdapter_card_list_people.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                        adapter.setOnClickListener(object :
                            RecyclerAdapter_card_list_people.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                //Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()
                                intent.putExtra("contentId", list_card_list_people[position].id)

                                startActivity(intent)
                                //contentId = ""
                            }
                        })
                    }
                })
            }
    }

}