package com.example.worktrip

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk

//  다른 액티비티보다 먼저 생성되어야 함
// 따라서 Application 클래스 생성한 뒤, 전역 변수로 SharedPreferences를 갖게 만듦

class SocketApplication:Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)

        super.onCreate()
        FirebaseApp.initializeApp(this)
        // Kakao SDK 초기화
        KakaoSdk.init(this,getString(R.string.kakao_app_key))

    }
}