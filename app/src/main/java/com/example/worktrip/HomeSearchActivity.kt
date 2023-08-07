package com.example.worktrip

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.databinding.ActivityHomeSearchBinding
import com.google.android.material.search.SearchBar

private lateinit var binding : ActivityHomeSearchBinding
class HomeSearchActivity : AppCompatActivity() {

    private lateinit var searchBar: SearchBar

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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼

        //intent
        var check = intent.getStringExtra("searchCheck")

        var checkCourse=findViewById<CheckBox>(R.id.cb_activity_home_search_course)
        var checkLodging=findViewById<CheckBox>(R.id.cb_activity_home_search_lodging)
        var checkProgram=findViewById<CheckBox>(R.id.cb_activity_home_search_program)
        var checkFood=findViewById<CheckBox>(R.id.cb_activity_home_search_food)

        //메뉴에 따른 체크박스 디폴트 설정
        when (check)
        {
            "course"->
            {
                checkCourse.isChecked=true
                checkLodging.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=false
            }

            "lodging"->
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=true
                checkProgram.isChecked=false
                checkFood.isChecked=false
            }

            "program"->
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkProgram.isChecked=true
                checkFood.isChecked=false
            }

            "food"->
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=true
            }
        }

        //체크박스 클릭리스너 & 라디오버튼화
        checkCourse.setOnClickListener{
            if (checkCourse.isChecked)
            {
                checkLodging.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=false

                Toast.makeText(applicationContext, "1", Toast.LENGTH_LONG).show()
            }
        }

        checkLodging.setOnClickListener{
            if (checkLodging.isChecked)
            {
                checkCourse.isChecked=false
                checkProgram.isChecked=false
                checkFood.isChecked=false

                Toast.makeText(applicationContext, "2", Toast.LENGTH_LONG).show()
            }
        }

        checkProgram.setOnClickListener{
            if (checkProgram.isChecked)
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkFood.isChecked=false

                Toast.makeText(applicationContext, "3", Toast.LENGTH_LONG).show()
            }
        }

        checkFood.setOnClickListener{
            if (checkFood.isChecked)
            {
                checkCourse.isChecked=false
                checkLodging.isChecked=false
                checkProgram.isChecked=false

                Toast.makeText(applicationContext, "4", Toast.LENGTH_LONG).show()
            }
        }

        //searchbar **findViewById 안 됨
        //searchBar=findViewById<SearchBar>(R.id.sv_activity_home_search)


        val spinner_location = findViewById<Spinner>(R.id.sp_activity_home_search_location)
        val locationItems = resources.getStringArray(R.array.locationItems)

        val SpinnerAdapter_location =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationItems)

        spinner_location.adapter = SpinnerAdapter_location

        //droplist를 spinner와 간격을 두고 나오도록
        //spinner_location.dropDownVerticalOffset = dipToPixels(500f).toInt()
        /*spinner_location.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게
                when (position) {
                    0 -> {

                    }

                    1 -> {

                    }
                    //...
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }*/
    }
    //dp to px
    /*private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }*/

    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_null, menu)
        return true
    }
}


