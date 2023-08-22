package com.example.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.Plan.Plan_workshop_details_Activity
import com.example.worktrip.databinding.CardPlanDetailItemBinding
import com.google.firebase.firestore.FirebaseFirestore


class PlanDetailTimeLineViewHolder(val binding: CardPlanDetailItemBinding)
    : RecyclerView.ViewHolder(binding.root){

    private val context = binding.root.context

    fun bind(item: PlanTimeLineData) {
        itemView.setOnClickListener { // 아이템 클릭
            val intent = Intent(context, Plan_workshop_details_Activity::class.java)
            intent.putExtra("data", item)
            intent.run { context.startActivity(this) }
        }
    }
}


class Plan_detail_timeline_Adapter(val context: Context, val itemList: MutableList<PlanTimeLineData>): RecyclerView.Adapter<PlanDetailTimeLineViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDetailTimeLineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)


        return PlanDetailTimeLineViewHolder(CardPlanDetailItemBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PlanDetailTimeLineViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.bind(data)
        // view랑 data 연결
        holder.binding.run {
           tvPlanDetailItemTimeHalf.setText(data.plan_time_half)
            tvPlanDetailItemTime.setText(data.plan_time)
            tvPlanDetailItemTitle.setText(data.plan_title)
            tvPlanDetailItemPlace.setText(data.plan_place)
            tvPlanDetailItemPresenter.setText(data.plan_presenter)
        }

    }
}
