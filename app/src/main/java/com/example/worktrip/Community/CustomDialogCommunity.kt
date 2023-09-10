package com.example.worktrip.Community

import android.content.Context
import android.view.WindowManager
import android.app.Dialog
import android.widget.Toast
import com.example.worktrip.My.firestore_bookmark_list
import com.example.worktrip.R
import kotlinx.android.synthetic.main.custom_dialog_community.btn_dialog_community_cancel
import kotlinx.android.synthetic.main.custom_dialog_community.btn_dialog_community_delete
import kotlinx.android.synthetic.main.custom_dialog_community.tv_dialog_community_title

class CustomDialogCommunity (context: Context){

    private val dialogCommunity = Dialog(context)
    private lateinit var onClickListener: onDialogClickListener

    fun setOnClickListener(listener: onDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialogCommunity.setContentView(R.layout.custom_dialog_community)
        dialogCommunity.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogCommunity.setCanceledOnTouchOutside(false)
        dialogCommunity.setCancelable(true)
        dialogCommunity.show()

        dialogCommunity.btn_dialog_community_cancel.setOnClickListener {
            dialogCommunity.dismiss()
        }

        dialogCommunity.btn_dialog_community_delete.setOnClickListener {
            //커뮤니티
            firestore_community.collection("community").document(dbWritingID).delete()
            firestore_community.collection("community").document(dbWritingID).collection("good").document().delete()

            //만약 누군가의 북마크에도 있다면
            firestore_bookmark_list.collection("user_bookmark").get()
                .addOnSuccessListener { task->
                    for (document in task)
                    {
                        var userID=document.data["userID"].toString()
                        firestore_bookmark_list.collection("user_bookmark").document(userID).collection("community").document(dbWritingID).delete()
                    }
                }

            onClickListener.onClicked(dialogCommunity.tv_dialog_community_title.toString())

            Toast.makeText(dialogCommunity.context, "삭제되었습니다.", Toast.LENGTH_LONG).show()

            dialogCommunity.dismiss()
            //DetailWritingActivity().finish()
        }
    }

    interface onDialogClickListener
    {
        fun onClicked(text: String)
    }

}