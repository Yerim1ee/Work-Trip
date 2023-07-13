package com.example.worktrip

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.databinding.ActivityMainBinding.bind

import com.example.worktrip.databinding.CardListBinding

data class data_card_list(
    var img: Bitmap?,
    var title: String,
    var location: String)

class RecyclerAdapter_card_list (private val items: ArrayList<data_card_list>) : RecyclerView.Adapter<RecyclerAdapter_card_list.ViewHolder>() {

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: CardListBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int {
        var size: Int = items.size
        if (size <= 50) size == 50

        return size
    }

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_list, parent, false)
        return ViewHolder(CardListBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //viewHolder.binding.ivCardListImg.setImageDrawable(items[position].img)
        viewHolder.binding.ivCardListImg.setImageBitmap(items[position].img)
        viewHolder.binding.tvCardListTitle.text = items[position].title
        viewHolder.binding.tvCardListLocation.text = items[position].location

        //view에 onClickListner를 달고, 그 안에서 직접 만든 itemClickListener를 연결
        viewHolder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)

            //val contentTitle= recyclerView_list.get(position)

        }

    }


    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int)

    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener


    //클릭리스너 등록 매소드
    fun setOnClickListener(itemClickListener: ItemClickListener) {
        itemClickListner = itemClickListener
    }

}


