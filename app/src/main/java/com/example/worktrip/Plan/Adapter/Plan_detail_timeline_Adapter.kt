package com.example.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.DataClass.PlanTimeLineData
import com.example.worktrip.Plan.PlanTimlineEditActivity
import com.example.worktrip.Plan.Plan_workshop_details_Activity
import com.example.worktrip.R
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.CardPlanDetailItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_plan_detail_item.view.ib_plan_detail_timeline_plus
import kotlinx.android.synthetic.main.card_plan_item.view.ib_plan_plus


class PlanDetailTimeLineViewHolder(val binding: CardPlanDetailItemBinding)
    : RecyclerView.ViewHolder(binding.root){
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val context = binding.root.context

    fun bind(item: PlanTimeLineData) {
        itemView.setOnClickListener { // 아이템 클릭
            val intent = Intent(context, Plan_workshop_details_Activity::class.java)
            intent.putExtra("data", item)
            intent.run { context.startActivity(this) }
        }
        itemView.ib_plan_detail_timeline_plus.setOnClickListener{
            val popup = PopupMenu(context, it, Gravity.RIGHT)
            popup.menuInflater.inflate(R.menu.popup_delete_edit1, popup.menu)
            popup.setOnMenuItemClickListener { menu ->
                when(menu.itemId) {
                    R.id.delete -> {
                        firestore_delete(item.docID.toString())
                    }
                    R.id.edit -> {
                        val intent = Intent(context, PlanTimlineEditActivity::class.java)
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

    fun firestore_delete(docID:String)
    {
        var timeline_docId = SocketApplication.prefs.getString("now_timeline_date", "") //
        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

        db.collection("workshop")
            .document(workshop_docID)
            .collection("date")
            .document(timeline_docId)
            .collection("timeline")
            .document(docID)
            .delete()
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
           tvPlanDetailItemTimeHalf.setText(data.plan_time_start_ampm)
            tvPlanDetailItemTime.setText(data.plan_time_start)
            tvPlanDetailItemTimeEnd.setText(data.plan_time_end)
            tvPlanDetailItemTimeHalfEnd.setText(data.plan_time_end_ampm)
            tvPlanDetailItemTitle.setText(data.plan_title)
            tvPlanDetailItemPlace.setText(data.plan_place)
            tvPlanDetailItemPresenter.setText(data.plan_presenter)
        }

    }
}
