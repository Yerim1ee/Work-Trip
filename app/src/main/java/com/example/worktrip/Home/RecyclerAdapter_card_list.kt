package com.example.worktrip.Home

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.example.worktrip.DataClass.dbContentId
import com.example.worktrip.DataClass.dbContentImage
import com.example.worktrip.DataClass.dbContentLocation
import com.example.worktrip.DataClass.dbContentOverview
import com.example.worktrip.DataClass.dbContentTitle
import com.example.worktrip.DataClass.dbContentTypeID
import com.example.worktrip.NetworkThread_detailCommon2
import com.example.worktrip.R
import com.example.worktrip.databinding.CardListBinding
import com.example.worktrip.detail_contentOverview
import com.google.firebase.auth.FirebaseAuth
//import com.example.worktrip.list_bitmap
import kotlinx.android.parcel.Parcelize


data class data_card_list(
    var img: String, //Bitmap?
    var title: String,
    var location: String,
    var id: String,
    var typeid: String
    )

private var isSaved=false

class RecyclerAdapter_card_list (private val items: ArrayList<data_card_list>) : RecyclerView.Adapter<RecyclerAdapter_card_list.ViewHolder>() {

    lateinit var mAuth: FirebaseAuth

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: CardListBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = items.size

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_list, parent, false)
        return ViewHolder(CardListBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        mAuth =FirebaseAuth.getInstance()
        //viewHolder.binding.ivCardListImg.setImageDrawable(items[position].img)
        //viewHolder.binding.ivCardListImg.setImageBitmap(items[position].img)
        Glide.with(viewHolder.binding.ivCardListImg).load(items[position].img).centerInside().into(viewHolder.binding.ivCardListImg)
        viewHolder.binding.tvCardListTitle.text = items[position].title
        viewHolder.binding.tvCardListLocation.text = items[position].location

        //추가_따로 불러와야 제대로 값이 저장됨
        var dbContentTypeID = "&contentTypeId="+items[position].typeid
        var dbContentTitle = items[position].title
        var dbContentOverview = ""
        var dbContentLocation = items[position].location
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
                        viewHolder.binding.ibCardListBookmark.isChecked=true
                        //onViewRecycled(viewHolder)
                        break
                    }
                    else{
                        viewHolder.binding.ibCardListBookmark.isChecked=false
                    }
                }
            }

        viewHolder.binding.ibCardListBookmark.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                if (isChecked)
                {

                    //정보를 파이어베이스에 저장
                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).set(data_bookmark_list)

                    //Toast.makeText(parent@ListRecommendedActivity(), "해당 정보가 북마크에 추가되었습니다.", Toast.LENGTH_LONG).show()
                    //isSaved=true
                }
                else
                {
                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).delete()
                    //Toast.makeText(parent@ListRecommendedActivity(), "해당 정보의 북마크를 제거했습니다.", Toast.LENGTH_LONG).show()
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

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
    }
    //

}


