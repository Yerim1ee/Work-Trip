package com.example.worktrip.Plan

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanWorkShopData
import com.example.worktrip.databinding.CardPlanItemBinding
import com.google.firebase.firestore.FirebaseFirestore


class PlanWorkshopViewHolder(val binding: CardPlanItemBinding) : RecyclerView.ViewHolder(binding.root)

class PlanWorkshopAdapter(val context: Context, val itemList: MutableList<PlanWorkShopData>): RecyclerView.Adapter<PlanWorkshopViewHolder>() {
    lateinit var db : FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanWorkshopViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        db = FirebaseFirestore.getInstance()

        return PlanWorkshopViewHolder(CardPlanItemBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        Log.d("lee",itemList.size.toString())
        return itemList.size
    }

    override fun onBindViewHolder(holder: PlanWorkshopViewHolder, position: Int) {
        val data = itemList.get(position)

        // 클릭 시 정보 넘기기
        holder.itemView.setOnClickListener{
            // Intent(context, PostActivity::class.java).apply{

                // 데이터 전달
                // putExtra("local",  holder.binding.localTextView.text.toString())

                // addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
           //  }.run{context.startActivity(this)}
        }

        // view랑 data 연결
        holder.binding.run {
            tvPlanDate.text = data.gettv_plan_date()
            tvPlanTitle.text = data.gettv_plan_title()
            tvPlanPeople.text = data.gettv_plan_people()
            tvPlanBudget.text = data.gettv_plan_budget()
            tvPlanFilter.text = data.gettv_plan_filter()
            ibPlanBtn1.setOnClickListener {

                // 페이지 넘기기
            }
            ibPlanBtn2.setOnClickListener {
                // 페이지 넘기기
            }
        }

    }
}
