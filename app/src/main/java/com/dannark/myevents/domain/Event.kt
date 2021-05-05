package com.dannark.myevents.domain

import android.os.Parcelable
import com.dannark.myevents.util.getCurrency
import com.dannark.myevents.util.smartTruncate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    val date: Long?,
    val description: String?,
    val image: String?,
    val longitude: String?,
    val latitude: String?,
    val price: Long?,
    val title: String?,
    val id: String,
) : Parcelable {
    val shortDescription: String
        get() = description?.smartTruncate(120) ?: ""

    val price_formatted: String
        get() = "${(price?:0).getCurrency()}"
}