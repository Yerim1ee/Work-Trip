package com.example.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.Plan.PlanBudgetEditActivity
import com.example.worktrip.Plan.PlanBudgetShowActivity
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.CardItemBudgetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_item_budget.view.ib_plan_card_detail_budget_plus


class PlanDetailBudgetViewHolder(val binding: CardItemBudgetBinding)
    : RecyclerView.ViewHolder(binding.root){
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth

    var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

    private val context = binding.root.context
    fun bind(item: PlanBudgetData) {
        db.collection("user_workshop")
            .document(auth.uid.toString())
            .collection("workshop_list")
            .document(workshop_docID)
            .get()
            .addOnSuccessListener {
                    result -> // 성공
                val item_result = result.toObject(PlanWorkShopUserData::class.java)
                if (item_result != null) {
                    if(item_result.part.toString().equals("참가자")){
                        itemView.ib_plan_card_detail_budget_plus.visibility = View.GONE
                    }
                    else{
                        itemView.ib_plan_card_detail_budget_plus.visibility = View.VISIBLE

                    }
                }
            }

        itemView.setOnClickListener { // 아이템 클릭
            val intent = Intent(context, PlanBudgetShowActivity::class.java)
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