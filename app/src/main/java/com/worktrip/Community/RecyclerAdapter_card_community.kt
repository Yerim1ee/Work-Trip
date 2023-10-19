package com.worktrip.Community

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.worktrip.My.firestore_bookmark_list
import com.worktrip.R
import com.worktrip.databinding.CardCommunityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

data class data_card_community(
    var img1: String, //Bitmap?
    var img2: String, //Bitmap?
    var img3: String, //Bitmap?
    var title: String,
    var content: String,
    var depature: String,
    var destination: String,
    var date: String,
    var company: String,
    var people: String,
    var period: String,
    var goal: String,
    var keyword: String,
    var money: String,
    //var good: String,
    var writingid: String,
    var userid: String)

class RecyclerAdapter_card_community  (private val items: ArrayList<data_card_community>)
    : RecyclerView.Adapter<RecyclerAdapter_card_community.ViewHolder>() {
    lateinit var mAuth: FirebaseAuth

    val storage = FirebaseStorage.getInstance("gs://work-trip-c01ab.appspot.com/")
    val storageRef = storage.reference

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: CardCommunityBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = items.size

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_community, parent, false)
        return ViewHolder(CardCommunityBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        mAuth = FirebaseAuth.getInstance()


        storageRef.child("community/${items[position].userid}/${items[position].writingid}_1.png")
            .downloadUrl
            .addOnSuccessListener { uri -> //이미지 로드 성공시
                    viewHolder.binding.ivCardCommunityImg.load(uri) }

        viewHolder.binding.tvCardCommunityTitle.text = items[position].title
        viewHolder.binding.tvCardCommunityDeparture.text = items[position].depature
        viewHolder.binding.tvCardCommunityDestination.text = items[position].destination
        viewHolder.binding.tvCardCommunityDate.text = items[position].date
        viewHolder.binding.tvCardCommunityCompany.text = items[position].company

        viewHolder.binding.tvCardCommunityPeriod.text = items[position].period
        viewHolder.binding.tvCardCommunityKeyword.text = items[position].keyword
        viewHolder.binding.tvCardCommunityPeople.text = items[position].people

        var dbWritingId=items[position].writingid


        /*if (items[position].good.equals("null") || items[position].good.equals(null) || items[position].good.equals("") || items[position].good.equals(0))
        {
            items[position].good="0"
        }*/

        //viewHolder.binding.tvCardCommunityGood.text = items[position].good

        firestore_community.collection("community").document(dbWritingId).collection("good").get()
            .addOnSuccessListener { task ->
                var commuListGood=0
                for (document in task) {
                    commuListGood += 1
                }
                viewHolder.binding.tvCardCommunityGood.text = commuListGood.toString()
            }

        if (!(items[position].img1.equals("없음")))
        {
            viewHolder.binding.ivCardCommunityNull.visibility= View.GONE
        }
        val data_bookmark = hashMapOf(
            "userID" to "${mAuth.currentUser?.uid.toString()}"
        )
        val data_bookmark_community= hashMapOf(
            "writingID" to dbWritingId,
            "date" to items[position].date,
            "company" to items[position].company,
            "depature" to items[position].depature,
            "destination" to items[position].destination,
            "people" to items[position].people,
            "period" to items[position].period,
            "goal" to items[position].goal,
            "keyword" to items[position].keyword,
            "money" to items[position].money,
            "img1" to items[position].img1,
            "img2" to items[position].img2,
            "img3" to items[position].img3,
            "title" to items[position].title,
            "content" to items[position].content,
            "userID" to items[position].userid
        )

        //view에 onClickListner를 달고, 그 안에서 직접 만든 itemClickListener를 연결
        viewHolder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
        }

        firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}")
            .collection("community").get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    var id = document.data["writingID"].toString() //필드 데이터
                    if (id.equals(items[position].writingid))
                    {
                        viewHolder.binding.cbCardCommunityBookmark.isChecked=true
                        //onViewRecycled(viewHolder)
                        break
                    }
                    else{
                        viewHolder.binding.cbCardCommunityBookmark.isChecked=false
                    }
                }
            }

        viewHolder.binding.cbCardCommunityBookmark.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                if (isChecked)
                {
                    viewHolder.binding.cbCardCommunityBookmark.isChecked=true
                    //정보를 파이어베이스에 저장
                    firestore_bookmark_list.collection("user_bookmark")
                        .document("${mAuth.currentUser?.uid.toString()}").set(data_bookmark)

                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("community").document(dbWritingId).set(data_bookmark_community)

                    //직접 클릭할때만 토스트가 보이도록
                    viewHolder.binding.cbCardCommunityBookmark.setOnClickListener{
                        Toast.makeText(viewHolder.itemView.context, "해당 정보가 북마크에 추가되었습니다.", Toast.LENGTH_LONG).show()
                    }
                    //isSaved=true
                }
                else
                {
                    viewHolder.binding.cbCardCommunityBookmark.isChecked=false

                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("community").document(dbWritingId).delete()

                    //직접 클릭할때만 토스트가 보이도록
                    viewHolder.binding.cbCardCommunityBookmark.setOnClickListener{
                        Toast.makeText(viewHolder.itemView.context, "해당 정보의 북마크를 제거했습니다.", Toast.LENGTH_LONG).show()
                    }
                    //isSaved=false

                }
            }
        })

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