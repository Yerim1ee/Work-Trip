package com.example.worktrip

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.worktrip.Home.DetailCourseActivity
import com.example.worktrip.Home.HomeSearchActivity
import com.example.worktrip.My.BookmarkActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.google.firebase.dynamiclinks.DynamicLink
//import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
//import com.google.firebase.dynamiclinks.PendingDynamicLinkData
//import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class BottomsheetShareUrl : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottomsheet_share_url, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.ib_bottomsheet_share_url_url)?.setOnClickListener {

            //Toast.makeText(DetailCourseActivity(), onDynamicLinkClick(DetailCourseActivity()).toString(), Toast.LENGTH_LONG).show()

            /*Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                    }

                    // Handle the deep link. For example, open the linked
                    // content, or apply promotional credit to the user's
                    // account.
                    // ...
                }
                .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }*/
        }



    }
    /*private fun getDeepLink(scheme: String, key: String?, id: String?): Uri {
        return if(key == null){
            Uri.parse("deep link url${scheme}")
        } else {
            Uri.parse("deep link url${scheme}/?${key}=$id")
        }
    }*/

    /*fun onDynamicLinkClick(
        activity: Activity,
        //scheme: String,
        //key: String? = null,
        //id: String? = null
    ) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://secret-wishbone-4f7.notion.site/Work-Trip-fec9b078cd944ccf9e618e986e530238?pvs=4"))
            .setDynamicLinkDomain("https://worktrip.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(activity.packageName)
                    .setMinimumVersion(1)
                    .build()
            )
            .buildShortDynamicLink()
            .addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    val shortLink: Uri = task.result.shortLink!!
                    try {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString())
                        sendIntent.type = "text/plain"
                        activity.startActivity(Intent.createChooser(sendIntent, "Share"))
                    } catch (ignored: ActivityNotFoundException) {
                    }
                } else {
                    Log.i(TAG, task.toString())
                }
            }
    }*/

    /*fun handleDynamicLinks(){
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deeplink: Uri? = null
                if(pendingDynamicLinkData != null) {
                    deeplink = pendingDynamicLinkData.link
                }

                /*if(deeplink != null) {
                    val segment: String = deeplink.lastPathSegment!!
                    when(segment){
                        SCHEME_PHEED -> {
                            //공유 타입이 pheed으로 들어왔을 때 처리
                            val code: String = deeplink.getQueryParameter(PARAM_ID)!!
                            Toast.makeText(this, "SCHEME_PHEED 타입 id : $code 데이터 보여주기", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SchemeActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        SCHEME_COMMENT -> {
                            //공유 타입이 comment로 들어왔을 때 처리
                            val code: String = deeplink.getQueryParameter(PARAM_ID)!!
                            Toast.makeText(this, "SCHEME_COMMENT 타입 id : $code 데이터 보여주기", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SchemeActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        SCHEME_MAIN -> {
                            //공유 타입이 main으로 들어왔을 때 처리
                            Toast.makeText(this, "메인 이동", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SchemeActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
                else {
                    Log.d(TAG, "getDynamicLink: no link found")
                }
            }*/
            .addOnFailureListener(this) { e -> Log.e(TAG, "getDynamicLink:onFailure", e) }*/
}