package com.one.fruitmanbuyer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Report(
    @SerializedName("fruit") var fruit : String? = null,
    @SerializedName("items") var items : MutableList<ReportItems> = mutableListOf()
) : Parcelable

@Parcelize
data class ReportItems(
    @SerializedName("month") var month : String? = null,
    @SerializedName("month_name") var month_name : String? = null,
    @SerializedName("value") var value : String? = null
) : Parcelable