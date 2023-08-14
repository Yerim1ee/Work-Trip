package com.example.worktrip.DataClass

import java.util.Date


data class UserBaseData (

    var userID : String? = null, // 파이어베이스에 의해 알아서 할당되는 고유 값
    var userpassword : String? = null, // password
    var kakaonumber : String? = null, // 카카오 회원번호
    var userName : String? = null,
    var date : String? = null,
    var company : String? = null,
    var alarm : Boolean = false
   // var food :BooleanArray = BooleanArray(6),
   // var travel : BooleanArray = BooleanArray(7),
  //  var sleep :BooleanArray = BooleanArray(9),
   // var reports : BooleanArray = BooleanArray(5),
   // var course : BooleanArray = BooleanArray(5)
){}
