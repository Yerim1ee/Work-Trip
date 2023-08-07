package com.example.worktrip

import android.graphics.Bitmap
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.worktrip.databinding.CardListPeopleBinding
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_card_list_people(
    var img: Bitmap?,
    var title: String,
    var people: String,
    var id: String):Parcelable

class RecyclerAdapter_card_list_people (private val items: ArrayList<data_card_list_people>) : RecyclerView.Adapter<RecyclerAdapter_card_list_people.ViewHolder>() {

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: CardListPeopleBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = items.size

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_list_people, parent, false)
        return ViewHolder(CardListPeopleBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //viewHolder.binding.ivCardListImg.setImageDrawable(items[position].img)
        viewHolder.binding.ivCardListPeopleImg.setImageBitmap(items[position].img)
        viewHolder.binding.tvCardListPeopleTitle.text = items[position].title
        viewHolder.binding.tvCardListPeoplePeople.text = items[position].people
        //viewHolder.binding.tvCardListLocation.text = items[position].id

        //추가
        //view에 onClickListner를 달고, 그 안에서 직접 만든 itemClickListener를 연결
        viewHolder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)

        }//

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
    //

}


