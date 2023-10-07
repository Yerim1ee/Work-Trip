package com.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.worktrip.DataClass.PlanNoticeData
import com.worktrip.DataClass.PlanWorkShopUserData
import com.worktrip.Plan.PlanNoticeEditActivity
import com.worktrip.Plan.PlanNoticeShowActivity
import com.worktrip.R
import com.worktrip.SocketApplication
import com.worktrip.databinding.CardItemNoticeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_item_notice.view.ib_card_notice_plus


class PlanDetailNoticeViewHolder(val binding: CardItemNoticeBinding)
    : RecyclerView.ViewHolder(binding.root){
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth

    var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")
    private val context = binding.root.context

    fun bind(item: PlanNoticeData) {
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
                        itemView.ib_card_notice_plus.visibility = View.GONE
                    }
                    else{
                        itemView.ib_card_notice_plus.visibility = View.VISIBLE

                    }
                }
            }

        itemView.setOnClickListener { // 아이템 클릭
            val intent = Intent(context, PlanNoticeShowActivity::class.java)
            intent.putExtra("data", item)
            intent.run { context.startActivity(this) }
        }

        itemView.ib_card_notice_plus.setOnClickListener {
            val popup = PopupMenu(context, it, Gravity.RIGHT)
            popup.menuInflater.inflate(R.menu.popup_delete_edit1, popup.menu)
            popup.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.delete -> {
                        firestore_delete(item.docID.toString())
                    }
                    R.id.edit -> {
                        val intent = Intent(context, PlanNoticeEditActivity::class.java)
                        intent.putExtra("data", item)
                        intent.run { context.startActivity(this)}

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
            .collection("notice")
            .document(docID)
            .delete()
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