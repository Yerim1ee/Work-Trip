package com.example.worktrip

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
        //스킴 딥링크
        val uri = Uri.parse("worktrip://detailcourse") //scheme://host, contain key ex. "worktrip://detailcourse?contentID=2361026"
        val i = Intent(Intent.ACTION_VIEW, uri)
        i.setPackage("com.example.worktrip")
        startActivity(i)

        view?.findViewById<ImageButton>(R.id.ib_bottomsheet_share_url_url)?.setOnClickListener {

        }

    }

}