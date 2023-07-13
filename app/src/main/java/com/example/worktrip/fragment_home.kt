package com.example.worktrip

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
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_home : Fragment() {
    //recyclerView
    val list1: ArrayList<data_card_image_title> = ArrayList()
    val list2: ArrayList<data_list_image_title_overview> = ArrayList()

    lateinit var recyclerView_recommendedCourse: RecyclerView
    lateinit var recyclerView_recommendedKeyword: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list1.add(data_card_image_title(null, "11111"))
        list1.add(data_card_image_title(null, "22222"))
        list1.add(data_card_image_title(null, "33333"))

        list2.add(data_list_image_title_overview(null, "11111", "11111111111111111111111111111111111111111111111"))
        list2.add(data_list_image_title_overview(null, "22222", "22222222222222222222222222222222222222222222222"))
        list2.add(data_list_image_title_overview(null, "33333", "33333333333333333333333333333333333333333333333"))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //recyclerView
        recyclerView_recommendedCourse=view.findViewById(R.id.rv_fragment_home_recommendedCourse!!)as RecyclerView
        recyclerView_recommendedCourse.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView_recommendedCourse.adapter=RecyclerAdapter_card_image_title(list1)

        //
        recyclerView_recommendedKeyword=view.findViewById(R.id.rv_fragment_home_recommendedKeyword!!)as RecyclerView
        recyclerView_recommendedKeyword.layoutManager=LinearLayoutManager(requireContext())
        recyclerView_recommendedKeyword.adapter=RecyclerAdapter_list_image_title_overview(list2)

        //category intent
        val categoryCorse = view.findViewById<ImageButton>(R.id.ib_fragment_home_cate_corse)
        categoryCorse.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, ListRecommendedActivity::class.java)
                startActivity(intent)
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
}