package com.worktrip.Plan.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.worktrip.Plan.Plan_tab_main01Fragment
import com.worktrip.Plan.Plan_tab_main02Fragment

private const val NUM_TABS = 2

class Plan_tab_main_Adapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return Plan_tab_main01Fragment()
            1 -> return Plan_tab_main02Fragment()
        }
        return Plan_tab_main02Fragment()
    }


}