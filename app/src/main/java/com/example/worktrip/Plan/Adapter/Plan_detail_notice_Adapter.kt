package com.example.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanNoticeData
import com.example.worktrip.Plan.Plan_workshop_details_Activity
import com.example.worktrip.databinding.CardItemBudgetBinding
import com.example.worktrip.databinding.CardItemNoticeBinding


class PlanDetailNoticeViewHolder(val binding: CardItemNoticeBinding)
    : RecyclerView.ViewHolder(binding.root){

    private val context = binding.root.context

    fun bind(item: PlanNoticeData) {
        itemView.setOnClickListener { // 아이템 클릭
            val intent = Intent(context, Plan_workshop_details_Activity::class.java)
            intent.putExtra("data", item)
            intent.run { context.startActivity(this) }
        }
    }

}

class Plan_detail_notice_Adapter (val context: Context, val itemList: MutableList<PlanNoticeData>): RecyclerView.Adapter<PlanDetailNoticeViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDetailNoticeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)


        return PlanDetailNoticeViewHolder(CardItemNoticeBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PlanDetailNoticeViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.bind(data)
        // view랑 data 연결
        holder.binding.run {
            tvCardNoticeContent.setText(data.plan_content)
            tvCardNoticePeople.setText(data.plan_people)
            tvCardNoticeTitle.setText(data.plan_title)

        }

    }
}