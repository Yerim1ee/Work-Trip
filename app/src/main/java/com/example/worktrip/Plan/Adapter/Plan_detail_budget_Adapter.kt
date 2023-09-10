package com.example.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.Plan.PlanBudgetEditActivity
import com.example.worktrip.Plan.PlanTimlineEditActivity
import com.example.worktrip.Plan.Plan_workshop_details_Activity
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.CardItemBudgetBinding
import com.example.worktrip.databinding.CardPlanDetailItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_item_budget.view.ib_plan_card_detail_budget_plus
import kotlinx.android.synthetic.main.card_plan_detail_item.view.ib_plan_detail_timeline_plus


class PlanDetailBudgetViewHolder(val binding: CardItemBudgetBinding)
    : RecyclerView.ViewHolder(binding.root){
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val context = binding.root.context

    fun bind(item: PlanBudgetData) {
        itemView.setOnClickListener { // 아이템 클릭
            val intent = Intent(context, Plan_workshop_details_Activity::class.java)
            intent.putExtra("data", item)
            intent.run { context.startActivity(this) }
        }

        itemView.ib_plan_card_detail_budget_plus.setOnClickListener {
            val popup = PopupMenu(context, it, Gravity.RIGHT)
            popup.menuInflater.inflate(R.menu.popup_delete_edit1, popup.menu)
            popup.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.delete -> {
                        firestore_delete(item.docID.toString())
                    }
                    R.id.edit -> {
                        val intent = Intent(context, PlanBudgetEditActivity::class.java)
                        intent.putExtra("data", item)
                        intent.run {
                            context.startActivity(this)
                        }
                    }
                }
                true
            }
            popup.show()
        }
    }


        fun firestore_delete(docID: String) {
            var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

            db.collection("workshop")
                .document(workshop_docID)
                .collection("budget")
                .document(docID)
                .delete()
        }
    }


    class Plan_detail_budget_Adapter (val context: Context, val itemList: MutableList<PlanBudgetData>): RecyclerView.Adapter<PlanDetailBudgetViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDetailBudgetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)


        return PlanDetailBudgetViewHolder(CardItemBudgetBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PlanDetailBudgetViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.bind(data)
        // view랑 data 연결
        holder.binding.run {
            tvPlanBudgetItemCategory.setText(data.plan_category)
            tvPlanBudgetItemPay.setText(data.plan_pay)
            tvPlanBudgetItemUsedContent.setText(data.plan_content)
            tvPlanBudgetItemUsedQuantity.setText(data.plan_quantity)
            tvPlanBudgetItemUsedMoney.setText(data.plan_price)
        }

    }
}