package com.example.worktrip.Home

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worktrip.My.bookmarkId
import com.example.worktrip.My.bookmarkTypeId
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.NetworkThread_detailCommon2
import com.example.worktrip.R
import com.example.worktrip.databinding.CardImageTitleBinding
import com.example.worktrip.detail_contentOverview
import com.google.firebase.auth.FirebaseAuth

/*data class data_card_list(
    var img: String, //Bitmap?
    var title: String,
    var location: String,
    var id: String,
    var typeid: String
)*/

class RecyclerAdapter_card_image_title(private val items: ArrayList<data_card_list>) : RecyclerView.Adapter<RecyclerAdapter_card_image_title.ViewHolder>() {
    lateinit var mAuth: FirebaseAuth

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: CardImageTitleBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = 3

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image_title, parent, false)
        return ViewHolder(CardImageTitleBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        mAuth = FirebaseAuth.getInstance()

        //viewHolder.binding.ivCardImageTitleImg.setImageBitmap(items[position].img)
        Glide.with(viewHolder.binding.ivCardImageTitleImg).load(items[position].img).centerInside().into(viewHolder.binding.ivCardImageTitleImg)
        viewHolder.binding.tvCardImageTitleTitle.text=items[position].title
        //view에 onClickListner를 달고, 그 안에서 직접 만든 itemClickListener를 연결
        viewHolder.itemView.setOnClickListener { itemClickListner.onClick(it, position) }

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

        firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    var id = document.data["contentID"].toString() //필드 데이터
                    if (id.equals(dbContentId))
                    {
                        viewHolder.binding.cbCardImageTitleBookmark.isChecked=true
                        //onViewRecycled(viewHolder)
                        break
                    }
                    else{
                        viewHolder.binding.cbCardImageTitleBookmark.isChecked=false
                    }
                }
            }

        viewHolder.binding.cbCardImageTitleBookmark.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                if (isChecked)
                {
                    viewHolder.binding.cbCardImageTitleBookmark.isChecked=true
                    //정보를 파이어베이스에 저장
                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).set(data_bookmark_list)
                    //overview 정보 추가 저장
                    updateOverview()

                    //직접 클릭할때만 토스트가 보이도록
                    viewHolder.binding.cbCardImageTitleBookmark.setOnClickListener{
                        Toast.makeText(viewHolder.itemView.context, "해당 정보가 북마크에 추가되었습니다.", Toast.LENGTH_LONG).show()
                    }
                    //isSaved=true
                }
                else
                {
                    viewHolder.binding.cbCardImageTitleBookmark.isChecked=false

                    firestore_bookmark_list.collection("user_bookmark").document("${mAuth.currentUser?.uid.toString()}").collection("list").document(dbContentId).delete()

                    //직접 클릭할때만 토스트가 보이도록
                    viewHolder.binding.cbCardImageTitleBookmark.setOnClickListener{
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

    fun updateOverview(){
        firestore_bookmark_list.collection("user_bookmark")
            .document("${mAuth.currentUser?.uid.toString()}").collection("list").get()
            .addOnSuccessListener { task ->
                for (document in task) {
                    bookmarkId = document.data["contentID"].toString() //필드 데이터
                    bookmarkTypeId = document.data["contentTypeID"].toString() //필드 데이터

                    if (document.data["contentOverview"].toString().equals("")) {
                        //overview 추가로 불러오기
                        val url_detailCommon2 =
                            "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D" + "&MobileOS=AND" + "&MobileApp=WorkTrip" + "&_type=xml&contentId=" + bookmarkId + bookmarkTypeId + "&defaultYN=N" + "&firstImageYN=N" + "&areacodeYN=N" + "&catcodeYN=N" + "&addrinfoYN=N" + "&mapinfoYN=N" + "&overviewYN=Y" + "&pageNo=1"
                        val thread_detailCommon2 =
                            Thread(NetworkThread_detailCommon2(url_detailCommon2))
                        thread_detailCommon2.start() // 쓰레드 시작
                        thread_detailCommon2.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        firestore_bookmark_list.collection("user_bookmark")
                            .document("${mAuth.currentUser?.uid.toString()}").collection("list")
                            .document(bookmarkId).update("contentOverview", detail_contentOverview)

                        detail_contentOverview =document.data["contentOverview"].toString()
                    }
                    else
                    {
                        bookmarkId =""
                        bookmarkTypeId =""
                    }
                }
            }
    }
}