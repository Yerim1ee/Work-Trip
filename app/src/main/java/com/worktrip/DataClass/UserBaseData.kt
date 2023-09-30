package com.worktrip.DataClass


data class UserBaseData (

    var userID : String? = null, // 파이어베이스에 의해 알아서 할당되는 고유 값
    var userpassword : String? = null, // password
    var kakaonumber : String? = null, // 카카오 회원번호
    var userName : String? = null,
    var date : String? = null,
    var company : String? = null,
   var food : String? = null,
   var travel : String? = null,
  var sleep : String? = null,
   var reports : String? = null,
   var course : String? = null,
){}
