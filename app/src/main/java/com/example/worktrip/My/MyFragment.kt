package com.example.worktrip.My

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.StateSet.TAG
import androidx.fragment.app.Fragment
import com.example.worktrip.SignUp.LoginActivity
import com.example.worktrip.SocketApplication
import com.example.worktrip.databinding.FragmentMyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MyFragment : Fragment() {
    lateinit var mAuth:FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMyBinding.inflate(inflater, container, false)

        binding.tvMyName.setText(SocketApplication.prefs.getString("user-name","익명"))

        mAuth =FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.ibMyLogout.setOnClickListener {
            signOut()
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

        }

        binding.layoutMySignOut.setOnClickListener {
            revokeAccess()
            Toast.makeText(getActivity(), "탈퇴 되었습니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

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