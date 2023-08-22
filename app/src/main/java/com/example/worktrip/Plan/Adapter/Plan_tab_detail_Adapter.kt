package com.example.worktrip.Plan.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.worktrip.Plan.Plan_detail_budget_Fragment
import com.example.worktrip.Plan.Plan_detail_notice_Fragment
import com.example.worktrip.Plan.Plan_detail_people_Fragment
import com.example.worktrip.Plan.Plan_detail_timeline_Fragment

private const val NUM_TABS = 4

class Plan_tab_detail_Adapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return Plan_detail_timeline_Fragment()
            1 -> return Plan_detail_budget_Fragment()
            2 -> return Plan_detail_people_Fragment()
            3 -> return Plan_detail_notice_Fragment()
        }

        return Plan_detail_timeline_Fragment()
    }
}