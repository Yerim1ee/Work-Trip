package com.example.worktrip.Plan

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.worktrip.DataClass.PlanWorkShopUserData
import com.example.worktrip.DataClass.UserBaseData
import com.example.worktrip.R
import com.example.worktrip.databinding.ActivityPlanStaticBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.eazegraph.lib.models.PieModel


class PlanStaticActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanStaticBinding

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    var camping: Double = 0.0
    var healed: Double = 0.0
    var eat: Double =0.0
    var family: Double = 0.0
    var walk: Double = 0.0
    var whole: Double = 0.0

    lateinit var docID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanStaticBinding.inflate(layoutInflater)
        setContentView(binding.root)

        docID = intent.getStringExtra("docID").toString()

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // auth 정의

        //toolbar 설정
        this.setSupportActionBar(findViewById(R.id.tb_static))
        supportActionBar!!.setDisplayShowTitleEnabled(false) //타이틀
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
        val toolbarTitle = binding.tvStaticTitle
        toolbarTitle.text = "팀원 통계"

        get_static()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData() {

        if((camping + eat + healed + family + walk) == whole){


                    camping = (camping/whole)*100
                    binding.tvStaticCamping.setText((camping).toInt().toString())




                    walk = (walk/whole)*100
                    binding.tvStaticWalk.setText((walk).toInt().toString())




                    eat = (eat/whole)*100
                    binding.tvStaticEat.setText((eat).toInt().toString())




                    healed = (healed/whole)*100
                    binding.tvStaticHealed.setText((healed).toInt().toString())




                    family = (family/whole)*100
                    binding.tvStaticFamily.setText((family).toInt().toString())



            // Set the data and color to the pie chart
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "힐링", binding.tvStaticHealed.text.toString().toFloat(),
                        Color.parseColor("#2757FF")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "맛", binding.tvStaticEat.text.toString().toFloat(),
                        Color.parseColor("#E1E8FF")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "캠핑", binding.tvStaticCamping.text.toString().toFloat(),
                        Color.parseColor("#FFA9A9")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "도보", binding.tvStaticWalk.text.toString().toFloat(),
                        Color.parseColor("#6688FF")
                    )
                )
            binding.pcStaticPiechart
                .addPieSlice(
                    PieModel(
                        "가족", binding.tvStaticFamily.text.toString().toFloat(),
                        Color.parseColor("#FFDADA")
                    )
                )

        }


    }

    private fun get_static(){

        db.collection("user_workshop")
            .get()
            .addOnSuccessListener { result1 ->
                for (document1 in result1) {
                    db.collection("user_workshop")
                        .document(document1.id)
                        .collection("workshop_list")
                        .whereEqualTo("workshop_docID", docID)
                        .get()
                        .addOnSuccessListener { result2 ->
                            for (document2 in result2) {
                                whole = whole + 1
                            }

                        }
                }

            }

        db.collection("user_workshop")
            .get()
            .addOnSuccessListener { result1 ->
                for (document1 in result1) {
                    db.collection("user_workshop")
                        .document(document1.id)
                        .collection("workshop_list")
                        .whereEqualTo("workshop_docID", docID)
                        .get()
                        .addOnSuccessListener { result2 ->
                            for (document2 in result2){
                                val item1 = document2.toObject(PlanWorkShopUserData::class.java)
                                db.collection("user")
                                    .document(item1.uID.toString())
                                    .get()
                                    .addOnSuccessListener {result3 ->
                                        val item2 = result3.toObject(UserBaseData::class.java)
                                        if (item2 != null) {
                                            when(item2.course){
                                                "캠핑 코스" ->{
                                                    camping = camping + 1
                                                }

                                                "도보 코스"->{
                                                    walk = walk + 1
                                                }

                                                "맛 코스"->{
                                                    eat = eat + 1
                                                }

                                                "힐링 코스"->{
                                                    healed = healed + 1
                                                }

                                                "가족 코스" -> {
                                                    family = family + 1
                                                }
                                            }
                                            setData()
                                        }


                                    }

                            }

                        }
                }

            }
    }

}