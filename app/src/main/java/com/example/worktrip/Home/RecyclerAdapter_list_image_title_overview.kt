package com.example.worktrip.Home

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worktrip.R
import com.example.worktrip.databinding.ListImageTitleOverviewBinding


data class data_list_image_title_overview(
    var img: String,
    var title: String,
    var overview: String)

class RecyclerAdapter_list_image_title_overview (private val items: ArrayList<data_list_image_title_overview>) : RecyclerView.Adapter<RecyclerAdapter_list_image_title_overview.ViewHolder>() {

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: ListImageTitleOverviewBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = 3

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_image_title_overview, parent, false)
        return ViewHolder(ListImageTitleOverviewBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //viewHolder.binding.ivListImageTitleOverviewImg.setImageBitmap(items[position].img)
        Glide.with(viewHolder.binding.ivListImageTitleOverviewImg).load(items[position].img).centerInside().into(viewHolder.binding.ivListImageTitleOverviewImg)
        viewHolder.binding.tvListImageTitleOverviewTitle.text=items[position].title
        viewHolder.binding.tvListImageTitleOverviewOverview.text=items[position].overview

    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
    }
}