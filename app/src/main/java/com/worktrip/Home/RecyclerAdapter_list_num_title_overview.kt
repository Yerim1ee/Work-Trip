package com.worktrip.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.worktrip.R
import com.worktrip.databinding.ListNumTitleOverviewBinding

data class data_list_num_title_overview(
    var num: String,
    var title: String,
    var overview: String,
    var img: String,
    var contentId: String,
    var contentTypeId: String,
    var subcontentId: String
)

class RecyclerAdapter_list_num_title_overview (private val items: ArrayList<data_list_num_title_overview>) : RecyclerView.Adapter<RecyclerAdapter_list_num_title_overview.ViewHolder>() {
    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: ListNumTitleOverviewBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = items.size

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_num_title_overview, parent, false)
        return ViewHolder(ListNumTitleOverviewBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.binding.tvListNumTitleOverviewNum.text = items[position].num
        viewHolder.binding.tvListNumTitleOverviewTitle.text = items[position].title
        viewHolder.binding.tvListNumTitleOverviewOverview.text = items[position].overview
        /*Glide.with(viewHolder.binding.ivListNumTitleOverviewImg).load(items[position].img)
            .centerInside().into(viewHolder.binding.ivListNumTitleOverviewImg)
        */
        viewHolder.binding.ivListNumTitleOverviewImg.load(items[position].img)



        if (position==0)
        {
            viewHolder.binding.vListNumTitleOverview.visibility=View.GONE
        }

        //view에 onClickListner를 달고, 그 안에서 직접 만든 itemClickListener를 연결
        viewHolder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
        }

    }

    //추가
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

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
    }
    //

}