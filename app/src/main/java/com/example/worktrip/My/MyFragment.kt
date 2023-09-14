package com.example.worktrip.My

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.StateSet.TAG
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMyBinding.inflate(inflater, container, false)

        db.collection("user")
            .document(mAuth.uid.toString())
            .get()
            .addOnSuccessListener { result -> // 성공
                val item = result.toObject(UserBaseData::class.java)
                if (item != null) {
                    binding.tvMyName.setText(item.userName)
                }
            }

        mAuth =FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.ibMyLogout.setOnClickListener {
            signOut()
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(activity, LoginActivity::class.java)
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
            startActivity(intent)

        }
        return binding.root
    }

    private fun signOut() {
       FirebaseAuth.getInstance().signOut()

    }

    private fun revokeAccess() {

        // db 에서 데이터 삭제
        db.collection("user").document("${mAuth.currentUser?.uid.toString()}")
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        mAuth.getCurrentUser()?.delete()
    }


}