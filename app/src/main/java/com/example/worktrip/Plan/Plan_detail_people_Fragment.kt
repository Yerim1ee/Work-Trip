package com.example.worktrip.Plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.worktrip.R
import com.example.worktrip.databinding.FragmentPlanDetailPeopleBinding
import com.example.worktrip.databinding.FragmentPlanDetailTimelineBinding


class Plan_detail_people_Fragment : Fragment() {


    private lateinit var binding: FragmentPlanDetailPeopleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanDetailPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

}