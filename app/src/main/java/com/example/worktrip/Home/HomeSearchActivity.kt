package com.example.worktrip.Home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.Community.CommuPlusActivity
import com.example.worktrip.Community.dbWritingID
import com.example.worktrip.NoticeActivity
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityHomeSearchBinding
import com.google.android.material.search.SearchBar

private lateinit var binding : ActivityHomeSearchBinding
class HomeSearchActivity : AppCompatActivity() {

    //private lateinit var searchBar: SearchBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_home_search)

        //toolbar 설정
        setSupportActionBar(findViewById(R.id.tb_activity_home_search))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(false) // 뒤로가기 버튼

        //intent
        var check = intent.getStringExtra("searchCheck")

        var searchBar=findViewById<EditText>(R.id.et_activity_home_search)

        var checkCourse=findViewById<CheckBox>(R.id.cb_activity_home_search_course)
        var checkLodging=findViewById<CheckBox>(R.id.cb_activity_home_search_lodging)
        var checkProgram=findViewById<CheckBox>(R.id.cb_activity_home_search_program)
        var checkFood=findViewById<CheckBox>(R.id.cb_activity_home_search_food)

        var goSearch=findViewById<Button>(R.id.btn_activity_home_search_ok)

        var searchText=""
        var searchCategory="선택하지 않음"
        var searchLocation=""

        var searchCategoryView="선택하지 않음"
        var searchLocationView="선택하지 않음"

        //메뉴에 따른 체크박스 디폴트 설정
        when (check)
        {
            "course"->
            {
                checkCourse.isChecked=true
                checkLodging.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=false

                searchCategory="&contentTypeId=25"
                searchCategoryView="코스"
            }

            "lodging"->
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=true
                checkProgram.isChecked=false
                checkFood.isChecked=false

                searchCategory="&contentTypeId=32"
                searchCategoryView="숙박"
            }

            "program"->
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkProgram.isChecked=true
                checkFood.isChecked=false

                searchCategory="program"
                searchCategoryView="프로그램"
            }

            "food"->
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=true

                searchCategory="&contentTypeId=39"
                searchCategoryView="맛집"
            }
        }

        //체크박스 클릭리스너 & 라디오버튼화
        checkCourse.setOnClickListener{
            if (checkCourse.isChecked) //코스
            {
                checkLodging.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=false

                //Toast.makeText(applicationContext, "1", Toast.LENGTH_LONG).show()
                searchCategory="&contentTypeId=25"
                searchCategoryView="코스"
            }
        }

        checkLodging.setOnClickListener{
            if (checkLodging.isChecked) //숙박
            {
                checkCourse.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=false

                //Toast.makeText(applicationContext, "2", Toast.LENGTH_LONG).show()
                searchCategory="&contentTypeId=32"
                searchCategoryView="숙박"
            }
        }

        checkProgram.setOnClickListener{
            if (checkProgram.isChecked) //프로그램
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkFood.isChecked=false

                //Toast.makeText(applicationContext, "3", Toast.LENGTH_LONG).show()
                searchCategory="program"
                searchCategoryView="프로그램"
            }
        }

        checkFood.setOnClickListener{
            if (checkFood.isChecked) //맛집
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkProgram.isChecked=false

                //Toast.makeText(applicationContext, "4", Toast.LENGTH_LONG).show()
                searchCategory="&contentTypeId=39"
                searchCategoryView="맛집"
            }
        }

        //searchbar **findViewById 안 됨
        //searchBar=findViewById<SearchBar>(R.id.sv_activity_home_search)

        //spinner
        val spinner_location = findViewById<Spinner>(R.id.sp_activity_home_search_location)
        val locationItemsNone = resources.getStringArray(R.array.locationItemsNone)

        val SpinnerAdapter_location =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationItemsNone)

        spinner_location.adapter = SpinnerAdapter_location

        spinner_location.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2)
                {
                    0->{ //선택하지 않음
                        //Toast.makeText(applicationContext, "0", Toast.LENGTH_LONG).show()
                        searchLocation="0"
                        searchLocationView="선택하지 않음"
                    }
                    1->{ //서울
                        //Toast.makeText(applicationContext, "1", Toast.LENGTH_LONG).show()
                        searchLocation="1"
                        searchLocationView="서울"
                    }
                    2->{ //인천
                        //Toast.makeText(applicationContext, "2", Toast.LENGTH_LONG).show()
                        searchLocation="2"
                        searchLocationView="인천"
                    }
                    3->{ //대전
                        //Toast.makeText(applicationContext, "3", Toast.LENGTH_LONG).show()
                        searchLocation="3"
                        searchLocationView="대전"
                    }
                    4->{ //대구
                        //Toast.makeText(applicationContext, "4", Toast.LENGTH_LONG).show()
                        searchLocation="4"
                        searchLocationView="대구"
                    }
                    5->{ //광주
                        //Toast.makeText(applicationContext, "5", Toast.LENGTH_LONG).show()
                        searchLocation="5"
                        searchLocationView="광주"
                    }
                    6->{ //부산
                        //Toast.makeText(applicationContext, "6", Toast.LENGTH_LONG).show()
                        searchLocation="6"
                        searchLocationView="부산"
                    }
                    7->{ //울산
                        //Toast.makeText(applicationContext, "7", Toast.LENGTH_LONG).show()
                        searchLocation="7"
                        searchLocationView="울산"
                    }
                    8->{ //세종특별자치시
                        //Toast.makeText(applicationContext, "8", Toast.LENGTH_LONG).show()
                        searchLocation="8"
                        searchLocationView="세종특별자치시"
                    }
                    9->{ //경기도
                        //Toast.makeText(applicationContext, "9", Toast.LENGTH_LONG).show()
                        searchLocation="31"
                        searchLocationView="경기도"
                    }
                    10->{ //강원특별자치도
                        //Toast.makeText(applicationContext, "10", Toast.LENGTH_LONG).show()
                        searchLocation="32"
                        searchLocationView="강원특별자치도"
                    }
                    11->{ //충청북도
                        //Toast.makeText(applicationContext, "11", Toast.LENGTH_LONG).show()
                        searchLocation="33"
                        searchLocationView="충청북도"
                    }
                    12->{ //충청남도
                        //Toast.makeText(applicationContext, "12", Toast.LENGTH_LONG).show()
                        searchLocation="34"
                        searchLocationView="충청남도"
                    }
                    13->{ //경상북도
                        //Toast.makeText(applicationContext, "13", Toast.LENGTH_LONG).show()
                        searchLocation="35"
                        searchLocationView="경상북도"
                    }
                    14->{ //경상남도
                        //Toast.makeText(applicationContext, "14", Toast.LENGTH_LONG).show()
                        searchLocation="36"
                        searchLocationView="경상남도"
                    }
                    15->{ //전라북도
                        //Toast.makeText(applicationContext, "15", Toast.LENGTH_LONG).show()
                        searchLocation="37"
                        searchLocationView="전라북도"
                    }
                    16->{ //전라남도
                        //Toast.makeText(applicationContext, "16", Toast.LENGTH_LONG).show()
                        searchLocation="38"
                        searchLocationView="전라남도"
                    }
                    17->{ //제주도
                        //Toast.makeText(applicationContext, "17", Toast.LENGTH_LONG).show()
                        searchLocation="39"
                        searchLocationView="제주도"
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        goSearch.setOnClickListener{
            searchText=searchBar.text.toString()
            var intent=Intent(this, HomeSearchListActivity::class.java)

            if (!(checkCourse.isChecked)&&!(checkLodging.isChecked)&&!(checkProgram.isChecked)&&!(checkFood.isChecked))
            {
                searchCategory="선택하지 않음"
                searchCategoryView="선택하지 않음"
                Toast.makeText(applicationContext, "카테고리를 선택해 주세요.", Toast.LENGTH_LONG).show()

            }

            else{
                intent.putExtra("searchText", searchText)
                intent.putExtra("searchCategory", searchCategory)
                intent.putExtra("searchLocation", searchLocation)

                intent.putExtra("searchCategoryView", searchCategoryView)
                intent.putExtra("searchLocationView", searchLocationView)


                startActivity(intent)
            }
        }

    }


    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_x, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.it_toolbar_x_clear -> {
                //x 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "x 실행", Toast.LENGTH_LONG).show()
                finish()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}


