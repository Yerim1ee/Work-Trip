package com.example.worktrip.Community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.databinding.ActivityCommuSearchBinding
import com.example.worktrip.databinding.ActivityHomeSearchBinding

private lateinit var binding: ActivityCommuSearchBinding

class CommuSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.worktrip.Community.binding = ActivityCommuSearchBinding.inflate(layoutInflater)
        val view = com.example.worktrip.Community.binding.root
        setContentView(view)
    }
}