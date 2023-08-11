package com.example.worktrip.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.Home.ListRecommendedActivity
import com.example.worktrip.NoticeActivity
import com.example.worktrip.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class fragment_home : Fragment() {
    //recyclerView
    val list1: ArrayList<data_card_image_title> = ArrayList()
    val list2: ArrayList<data_list_image_title_overview> = ArrayList()

    lateinit var recyclerView_recommendedCourse: RecyclerView
    lateinit var recyclerView_recommendedKeyword: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list1.add(data_card_image_title("null", "11111"))
        list1.add(data_card_image_title("null", "22222"))
        list1.add(data_card_image_title("null", "33333"))

        list2.add(data_list_image_title_overview("null", "11111", "11111111111111111111111111111111111111111111111"))
        list2.add(data_list_image_title_overview("null", "22222", "22222222222222222222222222222222222222222222222"))
        list2.add(data_list_image_title_overview("null", "33333", "33333333333333333333333333333333333333333333333"))

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //toolbar
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.tb_fragment_home))
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        setHasOptionsMenu(true)

        //recyclerView
        recyclerView_recommendedCourse=view.findViewById(R.id.rv_fragment_home_recommendedCourse!!)as RecyclerView
        recyclerView_recommendedCourse.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView_recommendedCourse.adapter= RecyclerAdapter_card_image_title(list1)

        //
        recyclerView_recommendedKeyword=view.findViewById(R.id.rv_fragment_home_recommendedKeyword!!)as RecyclerView
        recyclerView_recommendedKeyword.layoutManager=LinearLayoutManager(requireContext())
        recyclerView_recommendedKeyword.adapter= RecyclerAdapter_list_image_title_overview(list2)

        //category intent
        val categoryCourse = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_course)
        val categoryLodging = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_lodging)
        val categoryFood = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_food)
        val categoryProgram = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_program)

        var contentTypeId=""
        //var contentCat1=""
        //var contentCat2=""


        categoryCourse.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="&contentTypeId=25" //여행 코스
                //contentCat1="&cat1=C01" //분류 코드 1

                val intent = Intent(context, ListRecommendedActivity::class.java)
                intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)

                startActivity(intent)

                contentTypeId=""
                //contentCat1=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

        categoryLodging.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="&contentTypeId=32" //숙소
                //contentCat1="&cat1=B02" //분류 코드 1
                //contentCat2="&cat2=B0201" //분류 코드 2

                val intent = Intent(context, ListRecommendedActivity::class.java)
                intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)
                //intent.putExtra("contentCat2", contentCat2)

                startActivity(intent)

                contentTypeId=""
                //contentCat1=""
                //contentCat2=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

        categoryProgram.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="program" //프로그램
                val intent = Intent(context, ListProgramActivity::class.java)
                //intent.putExtra("contentTypeId", contentTypeId)
                startActivity(intent)
                contentTypeId=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

        categoryFood.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                contentTypeId="&contentTypeId=39" //맛집
                //contentCat1="&cat1=A05" //분류 코드 1
                //contentCat2="&cat1=A0502" //분류 코드 2

                val intent = Intent(context, ListRecommendedActivity::class.java)
                intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)
                //intent.putExtra("contentCat2", contentCat2)

                startActivity(intent)

                contentTypeId=""
                //contentCat1=""
                //contentCat2=""
                // 다른 액티비티에서 전환할 때
                // activity?.finish()
            }
        })

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_notice_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.it_toolbar_ns_notice -> {
                //알림 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "알림 실행", Toast.LENGTH_LONG).show()
                val intent = Intent(context, NoticeActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            R.id.it_toolbar_ns_search -> {
                //검색 버튼 눌렀을 때
                //Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                val intent = Intent(context, HomeSearchActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }
}