package com.example.worktrip.My

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worktrip.R
import com.example.worktrip.databinding.CardImageTitleOverviewLocationBinding
import com.example.worktrip.list_card_list
import com.google.firebase.auth.FirebaseAuth

data class data_card_image_title_overview_location(
    var img: String, //Bitmap?
    var title: String,
    var overview: String,
    var location: String,
    var id: String,
    var typeid: String)

class RecyclerAdapter_card_image_title_overview_location (private val items: ArrayList<data_card_image_title_overview_location>) : RecyclerView.Adapter<RecyclerAdapter_card_image_title_overview_location.ViewHolder>() {
    lateinit var mAuth: FirebaseAuth
    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: CardImageTitleOverviewLocationBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = items.size

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image_title_overview_location, parent, false)
        return ViewHolder(CardImageTitleOverviewLocationBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        mAuth = FirebaseAuth.getInstance()

        Glide.with(viewHolder.binding.ivCardImageTitleOverviewLocationImg).load(items[position].img).centerInside().into(viewHolder.binding.ivCardImageTitleOverviewLocationImg)
        viewHolder.binding.tvCardImageTitleOverviewLocationTitle.text = items[position].title
        viewHolder.binding.tvCardImageTitleOverviewLocationOverview.text = items[position].overview
        viewHolder.binding.tvCardImageTitleOverviewLocationLocation.text = items[position].location

        var dbContentId=items[position].id

        if (!(items[position].img.equals("")))
        {
            viewHolder.binding.ivCardImageTitleOverviewLocationNull.visibility=View.GONE
        }
        if (items[position].typeid.equals("program"))
        {
            viewHolder.binding.ivCardImageTitleOverviewLocationIcon.setBackgroundResource(R.drawable.icon_people)
        }

        //추가
        //view에 onClickListner를 달고, 그 안에서 직접 만든 itemClickListener를 연결
        viewHolder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
        }
        viewHolder.binding.ibCardImageTitleOverviewLocationBookmark.setOnClickListener{
            if (!(viewHolder.binding.ibCardImageTitleOverviewLocationBookmark.isChecked))
            {
                removeItem(position)
                firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).delete()
                Toast.makeText(viewHolder.itemView.context, "해당 정보의 북마크를 제거했습니다. (창을 재실행할 시 반영됩니다.)", Toast.LENGTH_LONG).show()
                //viewHolder.binding.cardImageTitleOverviewLocation.visibility=View.GONE
            }
        }
        /*viewHolder.binding.ibCardImageTitleOverviewLocationBookmark.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
            }
        })*/

    //

    }


    //추가
    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener


    //클릭리스너 등록 매소드
    fun setOnClickListener(itemClickListener: ItemClickListener) {
        itemClickListner = itemClickListener
    }

    fun removeItem(position: Int) {
        if (position > 0) {
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

}