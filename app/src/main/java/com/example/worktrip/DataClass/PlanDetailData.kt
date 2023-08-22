package com.example.worktrip.DataClass

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
data class PlanDetailDateData(
    @SerializedName("plan_timeline_currrent_docID") var docID : String? = null,

    )
@Parcelize
data class PlanTimeLineData (
    @SerializedName("plan_timeline_docID") var docID : String? = null,
    @SerializedName ("plan_timeline_title") var plan_title : String? = null,
    @SerializedName ("plan_timeline_presenter")  var plan_presenter : String? = null,
    @SerializedName ("plan_timeline_place") var plan_place : String? = null,
    @SerializedName ("plan_timeline_time_half") var plan_time_half : String? = null,
    @SerializedName ("plan_timeline_time") var plan_time : String? = null,
    @SerializedName ("plan_timeline_date") var plan_date : String? = null,
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(docID)
        parcel.writeString(plan_title)
        parcel.writeString(plan_presenter)
        parcel.writeString(plan_place)
        parcel.writeString(plan_time)
        parcel.writeString(plan_date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlanTimeLineData> {
        override fun createFromParcel(parcel: Parcel): PlanTimeLineData {
            return PlanTimeLineData(parcel)
        }

        override fun newArray(size: Int): Array<PlanTimeLineData?> {
            return arrayOfNulls(size)
        }
    }
}