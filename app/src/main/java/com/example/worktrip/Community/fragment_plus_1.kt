package com.example.worktrip.Community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.worktrip.R
import com.google.firebase.auth.FirebaseAuth
import com.example.worktrip.Community.firestore_community

var depature=""
var destination=""
var people=""
var period=""
var goal=""
var keyword=""
var money=""

class fragment_plus_1 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_plus_1, container, false)

        //spinner
        val spinner_depature = view.findViewById<Spinner>(R.id.sp_fragment_plus_1_depature)
        val locationItems = resources.getStringArray(R.array.locationItems)

        val SpinnerAdapter_location =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, locationItems)

        spinner_depature.adapter = SpinnerAdapter_location

        spinner_depature.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> { //서울
                        //Toast.makeText(context, "0", Toast.LENGTH_LONG).show()
                        depature="서울"
                    }

                    1 -> { //인천
                        //Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                        depature="인천"

                    }

                    2 -> { //대전
                        //Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
                        depature="대전"

                    }

                    3 -> { //대구
                        //Toast.makeText(context, "3", Toast.LENGTH_LONG).show()
                        depature="대구"

                    }

                    4 -> { //광주
                        //Toast.makeText(context, "4", Toast.LENGTH_LONG).show()
                        depature="광주"

                    }

                    5 -> { //부산
                        //Toast.makeText(context, "5", Toast.LENGTH_LONG).show()
                        depature="부산"

                    }

                    6 -> { //울산
                        //Toast.makeText(context, "6", Toast.LENGTH_LONG).show()
                        depature="울산"

                    }

                    7 -> { //세종특별자치시
                        //Toast.makeText(context, "7", Toast.LENGTH_LONG).show()
                        depature="세종특별자치시"

                    }

                    8 -> { //경기도
                        //Toast.makeText(context, "8", Toast.LENGTH_LONG).show()
                        depature="경기도"

                    }

                    9 -> { //강원특별자치도
                        //Toast.makeText(context, "9", Toast.LENGTH_LONG).show()
                        depature="강원특별자치도"

                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //
        val spinner_destination = view.findViewById<Spinner>(R.id.sp_fragment_plus_1_destination)
        spinner_destination.adapter = SpinnerAdapter_location

        spinner_destination.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> { //서울
                        //Toast.makeText(context, "0", Toast.LENGTH_LONG).show()
                        destination="서울"
                    }

                    1 -> { //인천
                        //Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                        destination="인천"

                    }

                    2 -> { //대전
                        //Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
                        destination="대전"

                    }

                    3 -> { //대구
                        //Toast.makeText(context, "3", Toast.LENGTH_LONG).show()
                        destination="대구"

                    }

                    4 -> { //광주
                        //Toast.makeText(context, "4", Toast.LENGTH_LONG).show()
                        destination="광주"

                    }

                    5 -> { //부산
                        //Toast.makeText(context, "5", Toast.LENGTH_LONG).show()
                        destination="부산"

                    }

                    6 -> { //울산
                        //Toast.makeText(context, "6", Toast.LENGTH_LONG).show()
                        destination="울산"

                    }

                    7 -> { //세종특별자치시
                        //Toast.makeText(context, "7", Toast.LENGTH_LONG).show()
                        destination="세종특별자치시"

                    }

                    8 -> { //경기도
                        //Toast.makeText(context, "8", Toast.LENGTH_LONG).show()
                        destination="경기도"

                    }

                    9 -> { //강원특별자치도
                        //Toast.makeText(context, "9", Toast.LENGTH_LONG).show()
                        destination="강원특별자치도"

                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //
        val spinner_people = view.findViewById<Spinner>(R.id.sp_fragment_plus_1_people)
        val peopleItems = resources.getStringArray(R.array.peopleItems)

        val SpinnerAdapter_people =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, peopleItems)

        spinner_people.adapter = SpinnerAdapter_people

        spinner_people.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> { //10명 이하
                        //Toast.makeText(context, "0", Toast.LENGTH_LONG).show()
                        people="10명 이하"
                    }

                    1 -> { //10~30명
                        //Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                        people="10~30명"

                    }

                    2 -> { //30~50명
                        //Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
                        people="30~50명"

                    }

                    3 -> { //50명 이상
                        //Toast.makeText(context, "3", Toast.LENGTH_LONG).show()
                        people="50명 이상"

                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //
        val spinner_period = view.findViewById<Spinner>(R.id.sp_fragment_plus_1_period)
        val periodItems = resources.getStringArray(R.array.periodItems)

        val SpinnerAdapter_period =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, periodItems)

        spinner_period.adapter = SpinnerAdapter_period

        spinner_period.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> { //당일치기
                        //Toast.makeText(context, "0", Toast.LENGTH_LONG).show()
                        period="당일치기"
                    }

                    1 -> { //1~2박
                        //Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                        period="1~2박"
                    }

                    2 -> { //3~5박
                        //Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
                        period="3~5박"
                    }

                    3 -> { //5박 이상
                        //Toast.makeText(context, "3", Toast.LENGTH_LONG).show()
                        period="5박 이상"
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //
        val spinner_goal = view.findViewById<Spinner>(R.id.sp_fragment_plus_1_goal)
        val goalItems = resources.getStringArray(R.array.goalItems)

        val SpinnerAdapter_goal =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, goalItems)

        spinner_goal.adapter = SpinnerAdapter_goal

        spinner_goal.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> { //워크숍
                        //Toast.makeText(context, "0", Toast.LENGTH_LONG).show()
                        goal="워크숍"
                    }

                    1 -> { //워케이션
                        //Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                        goal="워케이션"
                    }

                    2 -> { //단체여행
                        //Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
                        goal="단체여행"
                    }

                    3 -> { //MT
                        //Toast.makeText(context, "3", Toast.LENGTH_LONG).show()
                        goal="MT"
                    }

                    4 -> { //그 외
                        //Toast.makeText(context, "4", Toast.LENGTH_LONG).show()
                        goal="그 외"
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //
        val spinner_keyword = view.findViewById<Spinner>(R.id.sp_fragment_plus_1_keyword)
        val keywordItems = resources.getStringArray(R.array.keywordItems)

        val SpinnerAdapter_keyword =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, keywordItems)

        spinner_keyword.adapter = SpinnerAdapter_keyword

        spinner_keyword.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> { //자연/휴양
                        //Toast.makeText(context, "0", Toast.LENGTH_LONG).show()
                        keyword="자연/휴양"
                    }

                    1 -> { //관광/체험
                        //Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                        keyword="관광/체험"
                    }

                    2 -> { //역사/건축
                        //Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
                        keyword="역사/건축"
                    }

                    3 -> { //산업
                        //Toast.makeText(context, "3", Toast.LENGTH_LONG).show()
                        keyword="산업"
                    }

                    4 -> { //그 외
                        //Toast.makeText(context, "4", Toast.LENGTH_LONG).show()
                        keyword="그 외"
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //
        val spinner_money = view.findViewById<Spinner>(R.id.sp_fragment_plus_1_money)
        val moneyItems = resources.getStringArray(R.array.moneyItems)

        val SpinnerAdapter_money =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, moneyItems)

        spinner_money.adapter = SpinnerAdapter_money

        spinner_money.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> { //50만원 이하
                        //Toast.makeText(context, "0", Toast.LENGTH_LONG).show()
                        money="50만원 이하"
                    }

                    1 -> { //50~100만원
                        //Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                        money="50~100만원"
                    }

                    2 -> { //100~300만원
                        //Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
                        money="100~300만원"
                    }

                    3 -> { //300~500만원
                        //Toast.makeText(context, "3", Toast.LENGTH_LONG).show()
                        money="300~500만원"
                    }

                    4 -> { //500만원 이상
                        //Toast.makeText(context, "4", Toast.LENGTH_LONG).show()
                        money="500만원 이상"
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        return view
    }

}