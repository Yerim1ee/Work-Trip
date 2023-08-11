package com.example.worktrip.Home

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.worktrip.R
import com.example.worktrip.databinding.ListDistanceBinding
import com.example.worktrip.databinding.ListNumTitleLocationBinding
import kotlinx.android.parcel.Parcelize


data class data_list_ntl_d(
    var location_num: String,
    var location_title: String,
    var location_category: String,
    var location: String,
    var distance: String,
    var distance_time: String,

    var type: Int)

class RecyclerAdapter_detail_course : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val list = mutableListOf<data_list_ntl_d>()
    val LIST_NTL =0
    val LIST_D =1
    private lateinit var listNTLBinding: ListNumTitleLocationBinding
    private lateinit var listDBinding:ListDistanceBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LIST_NTL->{
                listNTLBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.list_num_title_location,parent,false)
                ListNTLViewHolder(listNTLBinding)
            }

            LIST_D->{
                listDBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.list_distance,parent,false)
                ListDViewHolder(listDBinding)
            }

            else->{
                throw RuntimeException("viewtype error")
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ListNTLViewHolder){
            holder.binding.tvListNumTitleLocationNum.text = list[position].location_num
            holder.binding.tvListNumTitleLocationTitle.text = list[position].location_title
            holder.binding.tvListNumTitleLocationCate.text = list[position].location_category
            holder.binding.tvListNumTitleLocationLocation.text = list[position].location

        }
        else if (holder is ListDViewHolder){
            holder.binding.tvListDistanceDistance.text = list[position].distance
            holder.binding.tvListDistanceTime.text = list[position].distance_time
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].type==LIST_NTL) LIST_NTL else LIST_D
    }


    inner class ListNTLViewHolder(val binding:ListNumTitleLocationBinding)
        :RecyclerView.ViewHolder(binding.root){
    }
    inner class ListDViewHolder(val binding:ListDistanceBinding)
        :RecyclerView.ViewHolder(binding.root){
    }

    fun addItem(item: data_list_ntl_d){
        list.add(item)
    }

}