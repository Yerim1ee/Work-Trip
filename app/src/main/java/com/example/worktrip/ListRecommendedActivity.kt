package com.example.worktrip

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.databinding.ActivityListRecommendedBinding
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

private lateinit var binding : ActivityListRecommendedBinding
val list_card_list: ArrayList<data_card_list> = ArrayList()
lateinit var recyclerView_list: RecyclerView

lateinit var intent: Intent

lateinit var bitmap: Bitmap
lateinit var contentTitle: String
lateinit var location: String


//var imgURL=""
//var img: RequestManager? = null

class ListRecommendedActivity  : AppCompatActivity() {
    private var adapter=RecyclerAdapter_card_list(list_card_list)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRecommendedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_list_recommended)

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_activity_list_recommended))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼


        //키 값
        val key = "599o%2FfnKg8hgR51clnKMjz0ZVncf2Gg%2FahikrqN3gDaUMlsAfyA80I%2BDNj40Q%2FKYQv66DOcIZ9OvOMg%2Fuq86IA%3D%3D"
        //한 페이지 결과 수
        val numOfRows ="&numOfRows=5"
        //페이지번호
        val pageNo="&pageNo=1"
        //AND(안드로이드)
        val mobileOS = "&MobileOS=AND"
        //서비스명 = 어플명
        val mobileApp = "&MobileApp=WorkTrip"
        //type (xml/json)
        val _type="&_type=xml"
        //목록구분(Y=목록, N=개수)
        val listYN="&listYN=Y"
        //정렬구분 (A=제목순, C=수정일순, D=생성일순) 대표 이미지가 반드시 있는 정렬(O=제목순, Q=수정일순, R=생성일순)
        val arrange="&arrange=O"
        //관광타입(12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
        val contentTypeId="&contentTypeId=25"

        //API 정보를 가지고 있는 주소
        val url = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey="+key+numOfRows+pageNo+mobileOS+mobileApp+_type+listYN+arrange+contentTypeId

        //쓰레드 생성
        val thread = Thread(NetworkThread(url))
        thread.start() // 쓰레드 시작
        thread.join() // 멀티 작업 안되게 하려면 start 후 join 입력

        //recycler view
        recyclerView_list=findViewById(R.id.rv_activity_list_recommended_list!!)as RecyclerView
        recyclerView_list.layoutManager= GridLayoutManager(this, 2)
        //recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
        recyclerView_list.adapter=adapter
        intent = Intent(this, DetailCourseActivity::class.java)

        adapter.setOnClickListener( object : RecyclerAdapter_card_list.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Toast.makeText(applicationContext, "${position}번 리스트 선택", Toast.LENGTH_LONG).show()

                intent.putExtra("title", contentTitle)
                intent.putExtra("location", location)


                startActivity(intent)

            }
        })


    }


    //toolbar
    //툴바 메뉴 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search, menu)
        return true
    }

    //툴바 아이콘 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.it_toolbar_s_search -> {
                //검색 버튼 눌렀을 때
                Toast.makeText(applicationContext, "검색 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    //
    fun listRefresh() {

        recyclerView_list.removeAllViewsInLayout()
        recyclerView_list.adapter=RecyclerAdapter_card_list(list_card_list)
    }



}

//api 연결
class NetworkThread(
    var url: String): Runnable {

    override fun run() {

        try {

            val xml : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            xml.documentElement.normalize()

            //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
            val list: NodeList = xml.getElementsByTagName("item")

            //list.length-1 만큼 얻고자 하는 태그의 정보를 가져온다
            for(i in 0..list.length-1){

                val n: Node = list.item(i)

                if(n.getNodeType() == Node.ELEMENT_NODE){
                    val elem = n as Element
                    val map = mutableMapOf<String,String>()


                    for(j in 0..elem.attributes.length - 1) {
                        map.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
                    }

                    var imgURL="${elem.getElementsByTagName("firstimage").item(0).textContent}"
                    var img : URLtoBitmapTask = URLtoBitmapTask()
                    img = URLtoBitmapTask().apply {
                        url = URL(imgURL)
                    }
                    bitmap = img.execute().get()


                    contentTitle="${elem.getElementsByTagName("title").item(0).textContent}"
                    location="${elem.getElementsByTagName("addr1").item(0).textContent}"

                    if ((location).equals(""))
                    {
                        location="주소 정보 없음"
                    }

                    list_card_list.add(data_card_list(bitmap, contentTitle, location))
                    //intent.putExtra("title", contentTitle.toString())

                }
            }
        } catch (e: Exception) {
            Log.d("TTT", "오픈API 에러: "+e.toString())
        }
    }

}

//url->bitmap
class URLtoBitmapTask() : AsyncTask<Void, Void, Bitmap>() {
    lateinit var url:URL
    override fun doInBackground(vararg params: Void?): Bitmap {
        val bitmap = BitmapFactory.decodeStream(url.openStream())
        return bitmap
    }
    override fun onPreExecute() {
        super.onPreExecute()

    }
    override fun onPostExecute(result: Bitmap) {
        super.onPostExecute(result)
    }
}

