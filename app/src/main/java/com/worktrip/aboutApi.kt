package com.worktrip

import com.worktrip.Home.data_card_list
import com.worktrip.Home.data_list_num_title_overview
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory
//*****스레드 클래스 안 for문이 작동하지 않으면 로그로 url 찍은 후 브라우저로 링크 이상 없는지 확인*****//

//상세 정보
var detail_contentTitle: String=""
var detail_contentLocation: String=""
//lateinit var detail_bitmap: Bitmap
var detail_imgURL: String=""

var detail_locationX: String="0.0"
var detail_locationY: String="0.0"

var detail_contentCat1:String=""
var detail_contentCat2:String=""
var detail_contentCat3:String=""

var detail_contentOverview: String=""

//공통정보조회
class NetworkThread_detailCommon1(
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

                    detail_imgURL="${elem.getElementsByTagName("firstimage").item(0).textContent}"
                    /*var img : URLtoBitmapTask = URLtoBitmapTask()
                    img = URLtoBitmapTask().apply {
                        url = URL(imgURL)
                    }
                    detail_bitmap = img.execute().get()*/

                    detail_contentTitle="${elem.getElementsByTagName("title").item(0).textContent}"
                    detail_contentLocation="${elem.getElementsByTagName("addr1").item(0).textContent}"
                    detail_contentOverview="${elem.getElementsByTagName("overview").item(0).textContent}"
                    detail_contentCat1="${elem.getElementsByTagName("cat1").item(0).textContent}"
                    detail_contentCat2="${elem.getElementsByTagName("cat2").item(0).textContent}"
                    detail_contentCat3="${elem.getElementsByTagName("cat3").item(0).textContent}"



                    if ((detail_contentLocation).equals(""))
                    {
                        detail_contentLocation="주소 정보 없음"
                        detail_locationX="0.0"
                        detail_locationY="0.0"
                    }

                    else
                    {
                        detail_locationX="${elem.getElementsByTagName("mapx").item(0).textContent}"
                        detail_locationY="${elem.getElementsByTagName("mapy").item(0).textContent}"
                    }
                }
            }
        } catch (e: Exception) {
            //"오픈API 에러: "+e.toString()
        }
    }
}

//공통정보조회(overview only)
class NetworkThread_detailCommon2(
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

                    detail_contentOverview="${elem.getElementsByTagName("overview").item(0).textContent}"
                }
            }
        } catch (e: Exception) {
            //"오픈API 에러: "+e.toString()
        }
    }
}

//코스+숙소+맛집 목록 (지역기반 관광정보조회)
val list_card_list: ArrayList<data_card_list> = ArrayList()
//lateinit var list_bitmap: Bitmap
lateinit var list_contentTitle: String
lateinit var list_contentLocation: String
lateinit var list_contentId: String
lateinit var list_contentTypeId: String


var list_imgURL=""

class NetworkThread_list(var url: String): Runnable { //, var activity: Activity)

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

                    list_imgURL="${elem.getElementsByTagName("firstimage").item(0).textContent}"

                    /*var img : URLtoBitmapTask = URLtoBitmapTask()
                    img = URLtoBitmapTask().apply {
                        url = URL(imgURL)
                    }
                    list_bitmap = img.execute().get()*/

                    //list_image=Glide.with(activity).load(imgURL).into(binding.ivCardListImg)

                    list_contentTitle="${elem.getElementsByTagName("title").item(0).textContent}"
                    list_contentLocation="${elem.getElementsByTagName("addr1").item(0).textContent}"
                    list_contentId="${elem.getElementsByTagName("contentid").item(0).textContent}"
                    list_contentTypeId="${elem.getElementsByTagName("contenttypeid").item(0).textContent}"


                    if ((list_contentLocation).equals(""))
                    {
                        list_contentLocation="주소 정보 없음"
                    }



                    list_card_list.add(data_card_list(list_imgURL, list_contentTitle, list_contentLocation, list_contentId, list_contentTypeId))

                }
            }
        } catch (e: Exception) {
            //"오픈API 에러: "+e.toString()
        }
    }

}

var detail_contentKeyword: String=""

//서비스분류코드조회
class NetworkThread_categoryCode1(
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
                    detail_contentKeyword="${elem.getElementsByTagName("name").item(0).textContent}"
                }
            }
        } catch (e: Exception) {
            //"오픈API 에러: "+e.toString()
        }
    }
}

var detail_contentFirstmenu: String=""
var detail_contentTreatmenu: String=""

var detail_contentTel: String=""
var detail_contentOpen: String=""
var detail_contentClose: String=""
var detail_contentParkingFod: String=""
var detail_contentReservationFod: String=""


//소개정보조회 (맛집)
class NetworkThread_detailIntroFood(
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

                    detail_contentFirstmenu="${elem.getElementsByTagName("firstmenu").item(0).textContent}"
                    detail_contentTreatmenu="${elem.getElementsByTagName("treatmenu").item(0).textContent}"

                    detail_contentTel="${elem.getElementsByTagName("infocenterfood").item(0).textContent}"
                    detail_contentOpen="${elem.getElementsByTagName("opentimefood").item(0).textContent}"
                    detail_contentClose="${elem.getElementsByTagName("restdatefood").item(0).textContent}"
                    detail_contentParkingFod="${elem.getElementsByTagName("parkingfood").item(0).textContent}"
                    detail_contentReservationFod="${elem.getElementsByTagName("reservationfood").item(0).textContent}"

                    if ((detail_contentTel).equals(""))
                    {
                        detail_contentTel="전화번호 정보 없음"
                    }
                    if ((detail_contentOpen).equals(""))
                    {
                        detail_contentOpen="영업시간 정보 없음"
                    }
                    if ((detail_contentClose).equals(""))
                    {
                        detail_contentClose="휴무일 정보 없음"
                    }
                    if ((detail_contentParkingFod).equals(""))
                    {
                        detail_contentParkingFod="주차 정보 없음"
                    }
                    if ((detail_contentReservationFod).equals(""))
                    {
                        detail_contentReservationFod="예약 정보 없음"
                    }
                }
            }
        } catch (e: Exception) {
            //"오픈API 에러: "+e.toString()
        }
    }
}

var detail_contentRoomCount: String=""
var detail_contentRoomType: String=""
var detail_contentSeminar: String=""

var detail_contentIn: String=""
var detail_contentOut: String=""
var detail_contentCook: String=""
var detail_contentParkingLod: String=""
var detail_contentTelReservationLod: String=""
var detail_contentTelDeskLod: String=""

var detail_contentUrl: String=""
var detail_contentUrlSplit= arrayOf<String>("")

//소개정보조회 (숙소)
class NetworkThread_detailIntroLodging(
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

                    detail_contentRoomCount="${elem.getElementsByTagName("roomcount").item(0).textContent}"
                    detail_contentRoomType="${elem.getElementsByTagName("roomtype").item(0).textContent}"
                    detail_contentSeminar="${elem.getElementsByTagName("seminar").item(0).textContent}"

                    detail_contentIn="${elem.getElementsByTagName("checkintime").item(0).textContent}"
                    detail_contentOut="${elem.getElementsByTagName("checkouttime").item(0).textContent}"
                    detail_contentCook="${elem.getElementsByTagName("chkcooking").item(0).textContent}"
                    detail_contentParkingLod="${elem.getElementsByTagName("parkinglodging").item(0).textContent}"
                    detail_contentTelReservationLod="${elem.getElementsByTagName("reservationlodging").item(0).textContent}"
                    detail_contentTelDeskLod="${elem.getElementsByTagName("infocenterlodging").item(0).textContent}"

                    //detail_contentUrl="${elem.getElementsByTagName("reservationurl").item(0).textContent}"

                    if ((detail_contentSeminar).equals(""))
                    {
                        detail_contentSeminar="세미나룸 정보 없음"
                    } else if ((detail_contentSeminar).equals("0"))
                        {
                            detail_contentSeminar="없음"
                        }
                    if ((detail_contentIn).equals(""))
                    {
                        detail_contentIn="체크인 정보 없음"
                    }
                    if ((detail_contentOut).equals(""))
                    {
                        detail_contentOut="체크아웃 정보 없음"
                    }
                    if ((detail_contentCook).equals(""))
                    {
                        detail_contentCook="취식여부 정보 없음"
                    }
                    if ((detail_contentParkingLod).equals(""))
                    {
                        detail_contentParkingLod="주차 정보 없음"
                    }
                    if ((detail_contentTelReservationLod).equals(""))
                    {
                        detail_contentTelReservationLod="예약 전화번호 정보 없음"
                    }
                    if ((detail_contentTelDeskLod).equals(""))
                    {
                        detail_contentTelDeskLod="데스크 전화번호 정보 없음"
                    }
                    /*if ((detail_contentUrl).equals(""))
                    {
                        detail_contentUrl="url 정보 없음"
                    }else if(!(detail_contentUrl.startsWith("http://"))) //<a href="http://...형식의 데이터 해결
                    {
                        var split=detail_contentUrl.split("\"")
                        detail_contentUrlSplit[0]= split[1]
                    }*/
                }
            }
        } catch (e: Exception) {
            //"오픈API 에러: "+e.toString()
        }
    }
}

val list_num_title_overview: ArrayList<data_list_num_title_overview> = ArrayList()
var detail_subnum=""
var detail_subtitle=""
var detail_suboverview=""
var detail_subImg=""
var detail_contentId=""
var detail_contentTypeId=""
var detail_subcontentId=""


//반복정보조회 (코스)
class NetworkThread_detailInfo1(var url: String): Runnable {

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

                    detail_subImg="${elem.getElementsByTagName("subdetailimg").item(0).textContent}"
                    /*var img : URLtoBitmapTask = URLtoBitmapTask()
                    img = URLtoBitmapTask().apply {
                        url = URL(imgURL)
                    }
                    detail_bitmap = img.execute().get()*/

                    detail_subnum=(i+1).toString()
                    detail_subtitle="${elem.getElementsByTagName("subname").item(0).textContent}"
                    detail_contentId="${elem.getElementsByTagName("contentid").item(0).textContent}"
                    detail_contentTypeId="${elem.getElementsByTagName("contenttypeid").item(0).textContent}"
                    detail_subcontentId="${elem.getElementsByTagName("subcontentid").item(0).textContent}"
                    detail_suboverview="${elem.getElementsByTagName("subdetailoverview").item(0).textContent}"

                    list_num_title_overview.add(data_list_num_title_overview(detail_subnum, detail_subtitle, detail_suboverview, detail_subImg, detail_contentId, detail_contentTypeId, detail_subcontentId))

                }
            }
        } catch (e: Exception) {
            //.d("TTT", "오픈API 에러: "+e.toString())
        }
    }
}
//val list_card_list: ArrayList<data_card_list> = ArrayList()

//lateinit var list_contentTitle: String
//lateinit var list_contentLocation: String
//lateinit var list_contentId: String
//lateinit var list_contentTypeId: String

//키워드 검색 조회
class NetworkThread_searchKeyword1(var url: String, var areaCode: String): Runnable {
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

                    if (areaCode.equals("0"))
                    {
                        list_imgURL="${elem.getElementsByTagName("firstimage").item(0).textContent}"

                        /*var img : URLtoBitmapTask = URLtoBitmapTask()
                        img = URLtoBitmapTask().apply {
                            url = URL(imgURL)
                        }
                        list_bitmap = img.execute().get()*/

                        //list_image=Glide.with(activity).load(imgURL).into(binding.ivCardListImg)

                        list_contentTitle="${elem.getElementsByTagName("title").item(0).textContent}"
                        list_contentLocation="${elem.getElementsByTagName("addr1").item(0).textContent}"
                        list_contentId="${elem.getElementsByTagName("contentid").item(0).textContent}"
                        list_contentTypeId="${elem.getElementsByTagName("contenttypeid").item(0).textContent}"

                        if ((list_contentLocation).equals(""))
                        {
                            list_contentLocation="주소 정보 없음"
                        }

                        list_card_list.add(data_card_list(list_imgURL, list_contentTitle, list_contentLocation, list_contentId, list_contentTypeId))

                    }
                    else{
                        if (areaCode.equals("${elem.getElementsByTagName("areacode").item(0).textContent}"))
                        {
                            list_imgURL="${elem.getElementsByTagName("firstimage").item(0).textContent}"

                            /*var img : URLtoBitmapTask = URLtoBitmapTask()
                            img = URLtoBitmapTask().apply {
                                url = URL(imgURL)
                            }
                            list_bitmap = img.execute().get()*/

                            //list_image=Glide.with(activity).load(imgURL).into(binding.ivCardListImg)

                            list_contentTitle="${elem.getElementsByTagName("title").item(0).textContent}"
                            list_contentLocation="${elem.getElementsByTagName("addr1").item(0).textContent}"
                            list_contentId="${elem.getElementsByTagName("contentid").item(0).textContent}"
                            list_contentTypeId="${elem.getElementsByTagName("contenttypeid").item(0).textContent}"

                            if ((list_contentLocation).equals(""))
                            {
                                list_contentLocation="주소 정보 없음"
                            }

                            list_card_list.add(data_card_list(list_imgURL, list_contentTitle, list_contentLocation, list_contentId, list_contentTypeId))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            //"오픈API 에러: "+e.toString()
        }
    }
}

//-----------------------------------------------------------------------------------
//url->bitmap
/*
class URLtoBitmapTask() : AsyncTask<Void, Void, Bitmap>() {
    lateinit var url: URL
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
*/