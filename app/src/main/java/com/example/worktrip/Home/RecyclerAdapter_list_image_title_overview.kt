package com.example.worktrip.Home

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.example.worktrip.My.bookmarkId
import com.example.worktrip.My.bookmarkTypeId
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.NetworkThread_detailCommon2
import com.example.worktrip.R
import com.example.worktrip.databinding.ListImageTitleOverviewBinding
import com.example.worktrip.detail_contentOverview


/*data class data_card_list(
    var img: String, //Bitmap?
    var title: String,
    var location: String,
    var id: String,
    var typeid: String
)*/

class RecyclerAdapter_list_image_title_overview (private val items: ArrayList<data_card_list>) : RecyclerView.Adapter<RecyclerAdapter_list_image_title_overview.ViewHolder>() {

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(val binding: ListImageTitleOverviewBinding) : RecyclerView.ViewHolder(binding.root)

    //아이템 개수
    override fun getItemCount(): Int = 3

    //뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_image_title_overview, parent, false)
        return ViewHolder(ListImageTitleOverviewBinding.bind(view))
    }

    //데이터 연결
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //viewHolder.binding.ivListImageTitleOverviewImg.setImageBitmap(items[position].img)
        /*Glide.with(viewHolder.binding.ivListImageTitleOverviewImg).load(items[position].img)
            .centerInside().into(viewHolder.binding.ivListImageTitleOverviewImg)
         */
        viewHolder.binding.ivListImageTitleOverviewImg.load(items[position].img)

        viewHolder.binding.tvListImageTitleOverviewTitle.text=items[position].title

        var contentID="&contentId="+items[position].id
        var contentTypeID="&contentTypeId="+items[position].typeid
        var overview=""

        viewHolder.itemView.setOnClickListener { itemClickListner.onClick(it, position) }

        //overview 추가로 불러오기
        if (!(items[position].id.equals(""))) {
            val url_detailCommon2 =
                "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D" + "&MobileOS=AND" + "&MobileApp=WorkTrip" + "&_type=xml" +
                        contentID + contentTypeID + "&defaultYN=N" + "&firstImageYN=N" + "&areacodeYN=N" + "&catcodeYN=N" + "&addrinfoYN=N" + "&mapinfoYN=N" + "&overviewYN=Y" + "&pageNo=1"
            val thread_detailCommon2 =
                Thread(NetworkThread_detailCommon2(url_detailCommon2))
            thread_detailCommon2.start() // 쓰레드 시작
            thread_detailCommon2.join() // 멀티 작업 안되게 하려면 start 후 join 입력

            overview=detail_contentOverview
        }
        viewHolder.binding.tvListImageTitleOverviewOverview.text=overview

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

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
    }
}