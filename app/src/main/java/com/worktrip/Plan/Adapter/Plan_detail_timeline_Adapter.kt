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
import com.worktrip.NetworkThread_detailCommon1
import com.worktrip.NetworkThread_weather
import com.worktrip.string
import kotlinx.android.synthetic.main.card_plan_detail_item.view.ib_plan_detail_timeline_plus
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


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
            //날짜
            val formatter0 = DateTimeFormatter.ofPattern("HH")
            val formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd")
            val formatter2 = SimpleDateFormat("yyyy.MM.dd")

            var timelinedateString =data.plan_date.toString()
            var timelinedate=formatter2.parse(timelinedateString) //일정
            var timelinedateApi=timelinedateString.replace(".", "")


            var today =  Calendar.getInstance().apply { //오늘
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            var todayString=LocalDateTime.now().format(formatter1)
            var timeInt=LocalDateTime.now().format(formatter0).toInt()

            //예정 일정 시간
            var timelineTimeApi=data.plan_time_start.toString().substring(0 until 2)+"00"

            var isWeather=false
            if (timelinedate.time>=today.time) //등록된 일정이 예정된 일정일 때만 예보를 보여줄 수 있도록
            {
                if (((timelinedate.time - today.time) / (24 * 60 * 60 * 1000)<=2))
                {
                    isWeather=true
                }
            }
            else{ //지남
                llPlanDetailItemWeather.visibility=View.GONE
            }


            if (data.plan_place.equals("")) //주소 정보 없음
            {
                llPlanDetailItemWeather.visibility=View.GONE
            }
            else
            {
                if (isWeather==true)
                {
                    string="" //초기화
                    //키 값
                    var key = "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
                    //페이지 번호
                    var pageNo = "&pageNo=1"
                    //한 페이지 결과 수
                    var numOfRows = "&numOfRows=1200"
                    //type (xml/json)
                    var dataType = "&dataType=XML"
                    //발표일
                    var base_date = "&base_date=" + todayString
                    //발표 시간
                    var base_time = "&base_time=" +"0200" //제일 첫 예측 시간

                    //각 지역의 중심 좌표로 설정 (매우 대략적임)
                    if (data.plan_place.toString().contains("서울")||data.plan_place.toString().contains("인천"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=37"
                        //예보 지점의 y좌표
                        var ny = "&ny=126"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }

                    else if (data.plan_place.toString().contains("대전")||data.plan_place.toString().contains("세종특별자치시"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=36"
                        //예보 지점의 y좌표
                        var ny = "&ny=127"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("대구"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=35"
                        //예보 지점의 y좌표
                        var ny = "&ny=128"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("광주광역시"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=35"
                        //예보 지점의 y좌표
                        var ny = "&ny=126"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("부산")||data.plan_place.toString().contains("울산"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=35"
                        //예보 지점의 y좌표
                        var ny = "&ny=129"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }

                    else if (data.plan_place.toString().contains("경기도"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=37"
                        //예보 지점의 y좌표
                        var ny = "&ny=127"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("강원"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=37"
                        //예보 지점의 y좌표
                        var ny = "&ny=128"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("충청북도"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=36"
                        //예보 지점의 y좌표
                        var ny = "&ny=127"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("충청남도"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=36"
                        //예보 지점의 y좌표
                        var ny = "&ny=126"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("경상북도"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=36"
                        //예보 지점의 y좌표
                        var ny = "&ny=128"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("경상남도"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=35"
                        //예보 지점의 y좌표
                        var ny = "&ny=128"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("전라북도"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=35"
                        //예보 지점의 y좌표
                        var ny = "&ny=127"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("전라남도"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=34"
                        //예보 지점의 y좌표
                        var ny = "&ny=126"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else if (data.plan_place.toString().contains("제주"))
                    {
                        //예보 지점의 x좌표
                        var nx = "&nx=33"
                        //예보 지점의 y좌표
                        var ny = "&ny=126"

                        //API 정보를 가지고 있는 주소
                        var url_weather =
                            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + key + pageNo + numOfRows + dataType + base_date + base_time + nx + ny
                        //쓰레드 생성
                        val thread_weather = Thread(NetworkThread_weather(url_weather, timelinedateApi, timelineTimeApi)) //url, 예정 일정 날짜, 예정 일정 시간
                        thread_weather.start() // 쓰레드 시작
                        thread_weather.join() // 멀티 작업 안되게 하려면 start 후 join 입력

                        tvPlanDetailItemWeather.setText(string)
                    }
                    else
                    {
                        llPlanDetailItemWeather.visibility=View.GONE
                    }
                }
                else
                {
                    llPlanDetailItemWeather.visibility=View.GONE
                }
            }
        }

    }
}
