package com.example.worktrip.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worktrip.R
import com.example.worktrip.databinding.ListNumTextBinding

data class data_list_num_text(
    var num: String,
    var text: String
)
class RecyclerAdapter_list_num_text  (private val items: ArrayList<data_list_num_text>) : RecyclerView.Adapter<RecyclerAdapter_list_num_text.ViewHolder>() {

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: ListNumTextBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = items.size

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_num_text, parent, false)
        return ViewHolder(ListNumTextBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.binding.tvListNumTextNum.text = items[position].num
        viewHolder.binding.tvListNumTextText.text = items[position].text
    }
}