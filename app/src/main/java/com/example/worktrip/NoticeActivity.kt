package com.example.worktrip

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.databinding.ActivityNoticeBinding


private lateinit var binding : ActivityNoticeBinding
class NoticeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = ActivityNoticeBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            //toolbar 설정
            setSupportActionBar(findViewById(R.id.tb_activity_notice))
            supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
            supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}