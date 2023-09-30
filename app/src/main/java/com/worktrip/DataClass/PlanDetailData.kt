package com.worktrip.DataClass

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
    @SerializedName ("plan_time_start") var plan_time_start : String? = null,
    @SerializedName ("plan_time_end") var plan_time_end : String? = null,
    @SerializedName ("plan_time_start_ampm") var plan_time_start_ampm : String? = null,
    @SerializedName ("plan_time_end_ampm") var plan_time_end_ampm : String? = null,
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.writeString(plan_time_start)
        parcel.writeString(plan_time_end)
        parcel.writeString(plan_time_start_ampm)
        parcel.writeString(plan_time_end_ampm)
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


data class PlanBudgetData (
    @SerializedName("plan_budget_docID") var docID : String? = null,
    @SerializedName ("plan_budget_price") var plan_price : String? = null,
    @SerializedName ("plan_budget_category")  var plan_category : String? = null,
    @SerializedName ("plan_budget_quantity") var plan_quantity : String? = null,
    @SerializedName ("plan_budget_pay") var plan_pay : String? = null,
    @SerializedName ("plan_budget_content") var plan_content : String? = null,
    @SerializedName ("plan_budget_recipt") var plan_recipt : String? = null,
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
        parcel.writeString(plan_price)
        parcel.writeString(plan_category)
        parcel.writeString(plan_quantity)
        parcel.writeString(plan_pay)
        parcel.writeString(plan_content)
        parcel.writeString(plan_recipt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlanBudgetData> {
        override fun createFromParcel(parcel: Parcel): PlanBudgetData {
            return PlanBudgetData(parcel)
        }

        override fun newArray(size: Int): Array<PlanBudgetData?> {
            return arrayOfNulls(size)
        }
    }
}


data class PlanNoticeData (
    @SerializedName("plan_notice_docID") var docID : String? = null,
    @SerializedName ("plan_notice_title") var plan_title : String? = null,
    @SerializedName ("plan_notice_people")  var plan_people : String? = null,
    @SerializedName ("plan_notice_content") var plan_content: String? = null,
    @SerializedName ("plan_notice_tag") var plan_tag: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(docID)
        parcel.writeString(plan_title)
        parcel.writeString(plan_people)
        parcel.writeString(plan_content)
        parcel.writeString(plan_tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlanNoticeData> {
        override fun createFromParcel(parcel: Parcel): PlanNoticeData {
            return PlanNoticeData(parcel)
        }

        override fun newArray(size: Int): Array<PlanNoticeData?> {
            return arrayOfNulls(size)
        }
    }
}

