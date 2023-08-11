package com.example.worktrip.Home

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worktrip.R
import com.example.worktrip.databinding.CardImageTitleBinding

data class data_card_image_title(
    var img: String,
    var title: String)


class RecyclerAdapter_card_image_title(private val items: ArrayList<data_card_image_title>) : RecyclerView.Adapter<RecyclerAdapter_card_image_title.ViewHolder>() {

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: CardImageTitleBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = 3

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image_title, parent, false)
        return ViewHolder(CardImageTitleBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //viewHolder.binding.ivCardImageTitleImg.setImageBitmap(items[position].img)
        Glide.with(viewHolder.binding.ivCardImageTitleImg).load(items[position].img).centerInside().into(viewHolder.binding.ivCardImageTitleImg)
        viewHolder.binding.tvCardImageTitleTitle.text=items[position].title

    }
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
    }

}