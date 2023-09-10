package com.example.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanBudgetData
import com.example.worktrip.DataClass.PlanNoticeData
import com.example.worktrip.DataClass.PlanWorkShopPeopleData
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.Plan.PlanBudgetEditActivity
import com.example.worktrip.Plan.Plan_workshop_details_Activity
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.CardItemNoticeBinding
import com.example.worktrip.databinding.CardPeopleItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.card_item_budget.view.ib_plan_card_detail_budget_plus
import kotlinx.android.synthetic.main.card_people_item.view.ib_plan_people_card_plus


class PlanDetailPeopleViewHolder(val binding: CardPeopleItemBinding)
    : RecyclerView.ViewHolder(binding.root){

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val context = binding.root.context

    fun bind(item: PlanWorkShopPeopleData) {
        itemView.ib_plan_people_card_plus.setOnClickListener { // 아이템 클릭
            val popup = PopupMenu(context, it, Gravity.RIGHT)
            popup.menuInflater.inflate(R.menu.popup_delete_edit1, popup.menu)
            popup.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.delete -> {
                        firestore_delete(item.person_uid.toString())
                    }
                    R.id.edit -> {
                        // 권한 수정 팝업창 띄우기!!
                    }
                }
                true
            }
            popup.show()
        }
    }

    private fun firestore_delete(person_uid: String) {
        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

        db.collection("user_workshop")
            .document(person_uid)
            .collection("workshop_list")
            .document(workshop_docID)
            .delete()
    }

}

class Plan_detail_person_Adapter (val context: Context, val itemList: MutableList<PlanWorkShopPeopleData>): RecyclerView.Adapter<PlanDetailPeopleViewHolder>()  {
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDetailPeopleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)


        return PlanDetailPeopleViewHolder(CardPeopleItemBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PlanDetailPeopleViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.bind(data)
        // view랑 data 연결
        holder.binding.run {
            tvPlanPeopleCardName.setText(getUserName(data.person_uid))
            if(data.part.equals("기획자")){
                tvPlanPeopleCardParticipant.visibility = View.INVISIBLE
                tvPlanPeopleCardManager.visibility = View.VISIBLE
            }
            else{
                tvPlanPeopleCardManager.visibility = View.INVISIBLE
                tvPlanPeopleCardParticipant.visibility = View.VISIBLE

            }

        }

    }

    private fun getUserName(personUid: String?): String {

        var userName:String = ""
        if (personUid != null) {
            db.collection("user")
                .document(personUid)
                .get()
                .addOnSuccessListener { result -> // 성공
                    val item = result.toObject(UserBaseData::class.java)
                    if (item != null) {
                        userName = item.userName.toString()
                    }

                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        return userName

    }
}