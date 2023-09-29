package com.example.worktrip.Home

import android.graphics.Bitmap
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.R

import com.example.worktrip.databinding.CardListPeopleBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.parcel.Parcelize


data class data_card_list_people(
    var id: String,
    var keyword: String,
    var title: String,
    var img: String,
    var people: String,
    var overview: String
    )

class RecyclerAdapter_card_list_people (private val items: ArrayList<data_card_list_people>) : RecyclerView.Adapter<RecyclerAdapter_card_list_people.ViewHolder>() {

    lateinit var mAuth: FirebaseAuth

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
        mAuth =FirebaseAuth.getInstance()

        //viewHolder.binding.ivCardListPeopleImg.setImageBitmap(items[position].img)
        /*Glide.with(viewHolder.binding.ivCardListPeopleImg).load(items[position].img)
            .centerInside().into(viewHolder.binding.ivCardListPeopleImg)

         */
        viewHolder.binding.ivCardListPeopleImg.load(items[position].img)

        viewHolder.binding.tvCardListPeopleTitle.text = items[position].title
        viewHolder.binding.tvCardListPeoplePeople.text = items[position].people
        //viewHolder.binding.tvCardListLocation.text = items[position].id

        var dbContentTypeID = "program"
        var dbContentTitle = items[position].title
        var dbContentOverview = items[position].overview
        var dbContentLocation = items[position].people
        var dbContentId = items[position].id
        var dbContentImage = items[position].img

        val data_bookmark_list= hashMapOf(
            "contentID" to dbContentId,
            "contentTitle" to dbContentTitle,
            "contentLocation" to dbContentLocation,
            "contentOverview" to dbContentOverview,
            "contentImage" to dbContentImage,
            "contentTypeID" to dbContentTypeID
        )
        //추가
        //view에 onClickListner를 달고, 그 안에서 직접 만든 itemClickListener를 연결
        viewHolder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)

        }
        firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    var id = document.data["contentID"].toString() //필드 데이터
                    if (id.equals(dbContentId))
                    {
                        viewHolder.binding.cbCardListPeopleBookmark.isChecked=true
                        //onViewRecycled(viewHolder)
                        break
                    }
                    else{
                        viewHolder.binding.cbCardListPeopleBookmark.isChecked=false
                    }
                }
            }

        viewHolder.binding.cbCardListPeopleBookmark.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                if (isChecked)
                {
                    viewHolder.binding.cbCardListPeopleBookmark.isChecked=true
                    //정보를 파이어베이스에 저장
                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).set(data_bookmark_list)

                    //직접 클릭할때만 토스트가 보이도록
                    viewHolder.binding.cbCardListPeopleBookmark.setOnClickListener{
                        Toast.makeText(viewHolder.itemView.context, "해당 정보가 북마크에 추가되었습니다.", Toast.LENGTH_LONG).show()
                    }
                    //isSaved=true
                }
                else
                {
                    viewHolder.binding.cbCardListPeopleBookmark.isChecked=false

                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).delete()

                    //직접 클릭할때만 토스트가 보이도록
                    viewHolder.binding.cbCardListPeopleBookmark.setOnClickListener{
                        Toast.makeText(viewHolder.itemView.context, "해당 정보의 북마크를 제거했습니다.", Toast.LENGTH_LONG).show()
                    }
                    //isSaved=false

                }
            }
        })
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
    //

}


