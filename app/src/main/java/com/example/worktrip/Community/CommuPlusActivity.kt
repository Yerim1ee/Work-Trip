package com.example.worktrip.Community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.databinding.ActivityCommuPlusBinding

private lateinit var binding: ActivityCommuPlusBinding

class CommuPlusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.worktrip.Community.binding = ActivityCommuPlusBinding.inflate(layoutInflater)
        val view = com.example.worktrip.Community.binding.root
        setContentView(view)
    }
}