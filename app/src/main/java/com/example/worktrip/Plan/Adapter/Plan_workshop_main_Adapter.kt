package com.example.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.Plan.Plan_workshop_details_Activity
import com.example.worktrip.Plan.Plan_workshop_edit_Activity
import com.example.worktrip.databinding.CardPlanItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_plan_item.view.ib_plan_btn2


class PlanWorkshopViewHolder(val binding: CardPlanItemBinding) : RecyclerView.ViewHolder(binding.root){

    private val context = binding.root.context

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(item: PlanWorkShopData) {
        itemView.ib_plan_btn2.setOnClickListener { // 아이템의 버튼2 클릭
            val intent = Intent(context, Plan_workshop_edit_Activity::class.java)
            intent.putExtra("data", item)
            intent.putExtra("budget", item.tv_plan_budget.toString())

            intent.run { context.startActivity(this) }
        }
        itemView.setOnClickListener { // 아이템 클릭
            val intent = Intent(context, Plan_workshop_details_Activity::class.java)
            intent.putExtra("title", item.tv_plan_title.toString());
            intent.putExtra("docID", item.docID);
            intent.run { context.startActivity(this) }
        }
    }
}

class PlanWorkshopAdapter(val context: Context, val itemList: MutableList<PlanWorkShopData>): RecyclerView.Adapter<PlanWorkshopViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanWorkshopViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)


        return PlanWorkshopViewHolder(CardPlanItemBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlanWorkshopViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.bind(data)
        // view랑 data 연결
        holder.binding.run {
            tvPlanDate.text = data.tv_plan_date_start + " ~ " + data.tv_plan_date_end
            tvPlanTitle.text = data.tv_plan_title
            tvPlanPeople.text = data.tv_plan_people
            tvPlanBudget.text = data.tv_plan_budget
            tvPlanFilter.text = data.tv_plan_filter
        }

    }
}
