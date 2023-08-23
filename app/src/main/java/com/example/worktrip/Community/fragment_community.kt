package com.example.worktrip.Community

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
import com.example.worktrip.Home.HomeSearchActivity
import com.example.worktrip.Home.ListRecommendedActivity
import com.example.worktrip.NoticeActivity
import com.example.worktrip.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_community.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_community : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_community, container, false)

        val plusWriting = view.findViewById<FloatingActionButton>(R.id.fb_fragment_community_write)
        plusWriting.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {

                val intent = Intent(context, CommuPlusActivity::class.java)
                //intent.putExtra("contentTypeId", contentTypeId)
                //intent.putExtra("contentCat1", contentCat1)

                startActivity(intent)
            }})
        return view
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
                val intent = Intent(context, CommuSearchActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }
}