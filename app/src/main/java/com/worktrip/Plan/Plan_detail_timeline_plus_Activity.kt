package com.worktrip.Plan

import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.worktrip.DataClass.PlanTimeLineData
import com.worktrip.R
import com.worktrip.SocketApplication
import com.worktrip.databinding.ActivityPlanDetailTimelinePlusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class Plan_detail_timeline_plus_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailTimelinePlusBinding


    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var time_start_ampm: String
    lateinit var time_start:String
    lateinit var time_end:String
    lateinit var time_end_ampm:String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailTimelinePlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        // workshop 게시글 번호 받아오기
        var workshop_docID = SocketApplication.prefs.getString("now_workshop_id", "")
        var timeline_docId = SocketApplication.prefs.getString("now_timeline_date", "") //
        var bookmark = SocketApplication.prefs.getString("from_to_bookmark", "") //

        if(bookmark.equals("timeline")){
            binding.etPlanDetailTimelinePlusTitle.setText(intent.getStringExtra("title"))
            binding.tvPlanDetailTimelinePlusPlace.setText(intent.getStringExtra("place"))
        }


        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_plan_detail_timeline_plus))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvPlanDetailTimelinePlusTitle
        toolbarTitle.text = "타임라인 추가"

        binding.ibPlanDetailTimelinePlusDate.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this,  R.style.timePickerStyle,TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                var hour = h

                when {
                    checkAmPm(h) == "PM" ->
                        hour -= 12
                }

                // 현재 화면에 보여지는 것 수정
                binding.tvPlanDetailTimelinePlusTime.setText(checkAmPm(h) + " %02d : %02d".format(hour,m))
                time_start = "%02d:%02d".format(h,m)
                time_start_ampm = checkAmPm(h)

            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }

        binding.ibPlanDetailTimelinePlusDateEnd.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, R.style.timePickerStyle, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                var hour = h
                when {
                    checkAmPm(h) == "PM" ->
                        hour -= 12
                    h == 0 ->
                        hour += 12
                }

                // 현재 화면에 보여지는 것 수정
                binding.tvPlanDetailTimelinePlusTimeEnd.setText(checkAmPm(h) + " %02d : %02d".format(hour,m))

                time_end = "%02d:%02d".format(h,m)
                time_end_ampm = checkAmPm(h)

            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }


        binding.btPlanPlusDone.setOnClickListener {
            if(!binding.tvPlanDetailTimelinePlusTime.text.toString().isEmpty() ||
                !binding.tvPlanDetailTimelinePlusTimeEnd.text.toString().isEmpty()){
                //문자열 LocalDateTime 으로 변환
                /*
                val timeParse_start: LocalTime = LocalTime.parse(time_start+":00")
                val timeParse_end: LocalTime = LocalTime.parse(time_end":00")
                if(!timeParse_start.isBefore(timeParse_end)){
                    Toast.makeText(this,"끝나는 시간을 시작 시간보다 나중으로 설정해주세요", Toast.LENGTH_LONG)
                }
                */
            }

            if (binding.etPlanDetailTimelinePlusTitle.text.toString().isEmpty()) {
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_LONG).show()
            } else if(binding.tvPlanDetailTimelinePlusTime.text.toString().isEmpty() ||
                binding.tvPlanDetailTimelinePlusTimeEnd.text.toString().isEmpty()){
                Toast.makeText(this, "시간을 설정해주세요", Toast.LENGTH_LONG).show()

            } else if (binding.tvPlanDetailTimelinePlusPlace.text.toString().isEmpty()) {
                Toast.makeText(this, "장소를 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (binding.tvPlanDetailTimelinePlusPresenter.text.toString().isEmpty()) {
                Toast.makeText(this, "진행자를 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else {

                var datetimelineData = PlanTimeLineData(
                    timeline_docId, // 타임라인의 아이디 만들어주기
                    binding.etPlanDetailTimelinePlusTitle.text.toString(),
                    binding.tvPlanDetailTimelinePlusPresenter.text.toString(),
                    binding.tvPlanDetailTimelinePlusPlace.text.toString(),
                    time_start,
                    time_end,
                    time_start_ampm,
                    time_end_ampm,
                    timeline_docId //날짜 추가
                ) // 데이터 구조


                // workshop 문서 생성
                db.collection("workshop")
                    .document(workshop_docID)
                    .collection("date")
                    .document(timeline_docId)
                    .collection("timeline")
                    .document()
                    .set(datetimelineData)
                    .addOnSuccessListener {
                        // 저장했다면 그 날짜의 타임라인 중
                        db.collection("workshop")
                            .document(workshop_docID)
                            .collection("date")
                            .document(timeline_docId)
                            .collection("timeline")
                            .get()
                            .addOnSuccessListener {
                                    result->
                                for (document in result) {
                                    val item = document.toObject(PlanTimeLineData::class.java)
                                    // docID가 실제 docID 로 변경되지 않은 경우
                                    if(item.docID.equals(timeline_docId)){
                                        // 변경
                                        db.collection("workshop")
                                            .document(workshop_docID)
                                            .collection("date")
                                            .document(timeline_docId)
                                            .collection("timeline")
                                            .document(document.id)
                                            .update("docID", document.id)
                                    }

                                }
                            }

                    }


                finish()
            }

            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkAmPm(hour:Int):String{
        var hour = hour
        var am_pm = ""
        // AM_PM decider logic
        when {hour == 0 -> {
            hour += 12
            am_pm = "AM"
        }
            hour == 12 -> am_pm = "PM"
            hour > 12 -> {
                hour -= 12
                am_pm = "PM"
            }
            else -> am_pm = "AM" }

        return am_pm
    }
}