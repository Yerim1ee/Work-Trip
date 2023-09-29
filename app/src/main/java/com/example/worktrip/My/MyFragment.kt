package com.example.worktrip.My

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.R
import com.example.worktrip.SignUp.LoginActivity
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.FragmentMyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MyFragment : Fragment() {
     var mAuth:FirebaseAuth = Firebase.auth
     var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var binding:FragmentMyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBinding.inflate(inflater, container, false)



        binding.ibMyLogout.setOnClickListener {
            signOut()
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

        }

        binding.layoutMySystemInformation.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/work-trip-c01ab.appspot.com/o/setting%2F%EC%9A%B4%EC%98%81-%EC%A0%95%EC%B1%85.html?alt=media&token=8ebc728d-94fe-49ac-a153-7f626c58232e"))
            startActivity(intent)
        }
        binding.layoutMyEditInformation.setOnClickListener {
            val intent = Intent(activity, My_change_info_Activity::class.java)
            startActivity(intent)
        }

        binding.layoutMySignOut.setOnClickListener {
            // Dialog만들기
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.custom_dialog3, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)

            val  mAlertDialog = mBuilder.show()

            val okButton = mDialogView.findViewById<Button>(R.id.btn_dialog_ok)
            okButton.setOnClickListener {
                revokeAccess()
                Toast.makeText(getActivity(), "탈퇴 되었습니다.", Toast.LENGTH_LONG).show()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)

            }

            val noButton = mDialogView.findViewById<Button>(R.id.btn_dialog_no)
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
            }


        }


        binding.layoutMyEditSave.setOnClickListener {
            val intent = Intent(activity, BookmarkActivity::class.java)
            var from = SocketApplication.prefs.setString("from_to_bookmark", "else")

            startActivity(intent)

        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        db.collection("user")
            .document(mAuth.uid.toString())
            .get()
            .addOnSuccessListener { result -> // 성공
                val item = result.toObject(UserBaseData::class.java)
                if (item != null) {
                    binding.tvMyName.setText(item.userName)
                    binding.tvMyContent.setText("# "+item.food +
                            " # "+item.course +
                            " # "+item.reports+
                            " # "+item.sleep+
                            " # "+item.travel)
                }
            }
    }

    private fun signOut() {
       FirebaseAuth.getInstance().signOut()

    }

    private fun revokeAccess() {

        // db 에서 데이터 삭제
        db.collection("user")
            .document("${mAuth.currentUser?.uid.toString()}")
            .delete()
            .addOnSuccessListener {  }
            .addOnFailureListener { }

        db.collection("user_workshop")
            .document("${mAuth.currentUser?.uid.toString()}")
            .delete()
            .addOnSuccessListener {
            }

        mAuth.getCurrentUser()?.delete()
    }


}