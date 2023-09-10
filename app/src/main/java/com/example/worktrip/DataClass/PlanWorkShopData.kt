package com.example.worktrip.DataClass

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlanWorkShopData(
    @SerializedName ("docID") var docID: String? = null,
    @SerializedName ("now") var now: Boolean = true,
    @SerializedName ("tv_plan_date_start") var tv_plan_date_start: String? = null,
    @SerializedName ("tv_plan_date_end") var tv_plan_date_end: String? = null,
    @SerializedName ("tv_plan_title") var tv_plan_title: String? = null,
    @SerializedName ("tv_plan_people") var tv_plan_people: String? = null,
    @SerializedName ("tv_plan_filter") var tv_plan_filter: String? = null,
    @SerializedName ("tv_plan_budget") var tv_plan_budget: String? = null,
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(docID)
        parcel.writeByte(if (now) 1 else 0)
        parcel.writeString(tv_plan_date_start)
        parcel.writeString(tv_plan_date_end)
        parcel.writeString(tv_plan_title)
        parcel.writeString(tv_plan_people)
        parcel.writeString(tv_plan_filter)
        parcel.writeString(tv_plan_budget)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlanWorkShopData> {
        override fun createFromParcel(parcel: Parcel): PlanWorkShopData {
            return PlanWorkShopData(parcel)
        }

        override fun newArray(size: Int): Array<PlanWorkShopData?> {
            return arrayOfNulls(size)
        }
    }

}


@Parcelize
data class PlanWorkShopUserData(
    @SerializedName ("workshop_docID") var workshop_docID: String? = null,
    @SerializedName ("start_date") var start_date: String? = null,
    @SerializedName ("part") var part: String? = null,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(workshop_docID)
        parcel.writeString(start_date)
        parcel.writeString(part)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlanWorkShopUserData> {
        override fun createFromParcel(parcel: Parcel): PlanWorkShopUserData {
            return PlanWorkShopUserData(parcel)
        }

        override fun newArray(size: Int): Array<PlanWorkShopUserData?> {
            return arrayOfNulls(size)
        }
    }

}


