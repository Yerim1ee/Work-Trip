package com.worktrip.Plan.Adapter

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.worktrip.DataClass.PlanTimeLineData
import com.worktrip.DataClass.PlanWorkShopUserData
import com.worktrip.Plan.PlanTimlineEditActivity
import com.worktrip.R
import com.worktrip.SocketApplication
import com.worktrip.databinding.CardPlanDetailItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_plan_detail_item.view.ib_plan_detail_timeline_plus


class PlanDetailTimeLineViewHolder(val binding: CardPlanDetailItemBinding)
    : RecyclerView.ViewHolder(binding.root){
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth

    var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")

    private val context = binding.root.context

    fun bind(item: PlanTimeLineData) {
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
                        itemView.ib_plan_detail_timeline_plus.visibility = View.GONE
                    }
                    else{
                        itemView.ib_plan_detail_timeline_plus.visibility = View.VISIBLE

                    }
                }
            }

        itemView.setOnClickListener { // 아이템 클릭
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

            //날씨 추가
            if (data.plan_place.equals(""))
            {
                llPlanDetailItemWeather.visibility=View.GONE
            }
            else
            {
                if (data.plan_place.toString().contains("서울"))
                {
                    tvPlanDetailItemWeather.setText("서울")
                }
                else if (data.plan_place.toString().contains("인천"))
                {
                    tvPlanDetailItemWeather.setText("인천")
                }
                else if (data.plan_place.toString().contains("대전"))
                {
                    tvPlanDetailItemWeather.setText("대전")
                }
                else if (data.plan_place.toString().contains("대구"))
                {
                    tvPlanDetailItemWeather.setText("대구")
                }
                else if (data.plan_place.toString().contains("광주광역시"))
                {
                    tvPlanDetailItemWeather.setText("광주광역시")
                }
                else if (data.plan_place.toString().contains("부산"))
                {
                    tvPlanDetailItemWeather.setText("부산")
                }
                else if (data.plan_place.toString().contains("울산"))
                {
                    tvPlanDetailItemWeather.setText("울산")
                }
                else if (data.plan_place.toString().contains("세종특별자치시"))
                {
                    tvPlanDetailItemWeather.setText("세종특별자치시")
                }
                else if (data.plan_place.toString().contains("경기도"))
                {
                    tvPlanDetailItemWeather.setText("경기도")
                }
                else if (data.plan_place.toString().contains("강원"))
                {
                    tvPlanDetailItemWeather.setText("강원")
                }
                else if (data.plan_place.toString().contains("충청북도"))
                {
                    tvPlanDetailItemWeather.setText("충청북도")
                }
                else if (data.plan_place.toString().contains("충청남도"))
                {
                    tvPlanDetailItemWeather.setText("충청남도")
                }
                else if (data.plan_place.toString().contains("경상북도"))
                {
                    tvPlanDetailItemWeather.setText("경상북도")
                }
                else if (data.plan_place.toString().contains("경상남도"))
                {
                    tvPlanDetailItemWeather.setText("경상남도")
                }
                else if (data.plan_place.toString().contains("전라북도"))
                {
                    tvPlanDetailItemWeather.setText("전라북도")
                }
                else if (data.plan_place.toString().contains("전라남도"))
                {
                    tvPlanDetailItemWeather.setText("전라남도")
                }
                else if (data.plan_place.toString().contains("제주"))
                {
                    tvPlanDetailItemWeather.setText("제주")
                }
                else
                {
                    llPlanDetailItemWeather.visibility=View.GONE
                }
            }
        }

    }
}
