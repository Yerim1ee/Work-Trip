package com.example.worktrip.Community

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Gallery
import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.bumptech.glide.Glide
import com.example.worktrip.R
import com.google.firebase.auth.FirebaseAuth
import java.net.URI

var commuTitle=""
var commuContent=""

var commuImage1=""
var commuImage2=""
var commuImage3=""

//lateinit var commuImageUri1: Uri


val GALLERY_CODE=101
lateinit var image1: ImageView
lateinit var nullImage1: ImageView
lateinit var nullText1: TextView

lateinit var image2: ImageView
lateinit var nullImage2: ImageView
lateinit var nullText2: TextView

lateinit var image3: ImageView
lateinit var nullImage3: ImageView
lateinit var nullText3: TextView

lateinit var countImg: TextView

private var isImage=0


class fragment_plus_2 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_plus_2, container, false)

        image1=view.findViewById<ImageView>(R.id.iv_fragment_plus_2_1)
        nullImage1=view.findViewById<ImageView>(R.id.iv_fragment_plus_2_null1)
        nullText1=view.findViewById<TextView>(R.id.tv_fragment_plus_2_null1)

        image2=view.findViewById<ImageView>(R.id.iv_fragment_plus_2_2)
        nullImage2=view.findViewById<ImageView>(R.id.iv_fragment_plus_2_null2)
        nullText2=view.findViewById<TextView>(R.id.tv_fragment_plus_2_null2)

        image3=view.findViewById<ImageView>(R.id.iv_fragment_plus_2_3)
        nullImage3=view.findViewById<ImageView>(R.id.iv_fragment_plus_2_null3)
        nullText3=view.findViewById<TextView>(R.id.tv_fragment_plus_2_null3)

        countImg=view.findViewById<TextView>(R.id.tv_fragment_plus_2_countImg)

        if (editWriting.equals("editWriting"))
        {

            view.findViewById<EditText>(R.id.et_fragment_plus_2_title).setText(editTitle)
            view.findViewById<EditText>(R.id.et_fragment_plus_2_content).setText(editContent)

            if (!(editImg1.equals("없음"))) {
                //image1.visibility = View.VISIBLE
                nullImage1.visibility = View.GONE
                nullText1.visibility = View.GONE
                commuImage1= editImg1

                // Glide.with(this).load(commuImage1).centerInside()
                    // .into(image1)
                image1.load(commuImage1)

                countImg.text="1/3"

            }

            if (!(editImg2.equals("없음"))) {
                //image2.visibility = View.VISIBLE
                nullImage2.visibility = View.GONE
                nullText2.visibility = View.GONE
                commuImage2= editImg2

                // Glide.with(this).load(commuImage2).centerInside()
                  //  .into(image2)
                image2.load(commuImage2)
                countImg.text="2/3"

            }


            if (!(editImg3.equals("없음"))) {
                //image3.visibility = View.VISIBLE
                nullImage3.visibility = View.GONE
                nullText3.visibility = View.GONE
                commuImage3= editImg3

                // Glide.with(this).load(commuImage3).centerInside()
                   // .into(image3)
                image3.load(commuImage3)
                countImg.text="3/3"

            }
        }

        image1.setOnClickListener{
            isImage=1
            val intent=Intent(Intent.ACTION_PICK)
            intent.type=MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, GALLERY_CODE)
        }

        image2.setOnClickListener{
            isImage=2
            val intent=Intent(Intent.ACTION_PICK)
            intent.type=MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, GALLERY_CODE)
        }

        image3.setOnClickListener{
            isImage=3
            val intent=Intent(Intent.ACTION_PICK)
            intent.type=MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, GALLERY_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==RESULT_OK)
        {
            when (requestCode){
                GALLERY_CODE->{
                        data?.data?.let { uri ->
                            if (isImage==1) {
                            image1.setImageURI(uri)
                            commuImage1 = uri.toString()
                            //commuImageUri1=uri

                            nullImage1.visibility = View.GONE
                            nullText1.visibility = View.GONE
                                countImg.text="1/3"

                            }
                            else if (isImage==2) {
                                image2.setImageURI(uri)
                                commuImage2 = uri.toString()

                                nullImage2.visibility = View.GONE
                                nullText2.visibility = View.GONE
                                countImg.text="2/3"

                            }
                            else if (isImage==3) {
                                image3.setImageURI(uri)
                                commuImage3 = uri.toString()

                                nullImage3.visibility = View.GONE
                                nullText3.visibility = View.GONE
                                countImg.text="3/3"

                            }
                    }

                    } } } }
}