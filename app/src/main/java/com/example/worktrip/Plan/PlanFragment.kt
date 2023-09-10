package com.example.worktrip.Plan

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.worktrip.MainActivity
import com.example.worktrip.Plan.Adapter.Plan_tab_main_Adapter
import com.example.worktrip.R
import com.example.worktrip.databinding.FragmentPlanBinding
import com.google.android.material.tabs.TabLayoutMediator


class PlanFragment : Fragment() {

    private lateinit var binding: FragmentPlanBinding
    private val tabTitleArray = arrayOf(
        "진행 워크숍",
        "지난 워크숍"
    )
    // activity 에서 context 가져오기
    var myContext: MainActivity = MainActivity()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanBinding.inflate(inflater, container, false)
        val viewPager = binding.vpPlanViewpager
        val tabLayout = binding.tlPlanTab
        viewPager.setSaveEnabled(false)

        // context 받아와서 getSupportFragmentManager()로 연결하는거 불가능
        // getChildFragmentManager() 받아와야 가능(fragment 안에 fragmet이기 때문에)
        viewPager.adapter = Plan_tab_main_Adapter( getChildFragmentManager(), lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]

        }.attach()


        binding.ibPlanPlus.setOnClickListener {
            activity?.let{
                /*
                // Dialog만들기
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog01, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)

                val  mAlertDialog = mBuilder.show()

                val okButton = mDialogView.findViewById<ImageButton>(R.id.ib_dialog_done)
                okButton.setOnClickListener {
                    resetPassword(mDialogView.findViewById<EditText>(R.id.et_dialog_email).text.toString())
                }

                val noButton = mDialogView.findViewById<ImageButton>(R.id.ib_dialog_back)
                noButton.setOnClickListener {
                    mAlertDialog.dismiss()
                }

                 */

                val intent = Intent(context, Plan_workshop_plus_Activity::class.java)
                startActivity(intent)
            }
        }
        return binding.root
    }


}