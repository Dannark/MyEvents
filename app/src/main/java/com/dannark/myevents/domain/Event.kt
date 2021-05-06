package com.dannark.myevents.domain

import android.os.Parcelable
import com.dannark.myevents.database.EventEntity
import com.dannark.myevents.network.EventNetwork
import com.dannark.myevents.util.getCurrency
import com.dannark.myevents.util.smartTruncate
import com.squareup.moshi.JsonClass
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
        get() = (price?:0).getCurrency()

    fun asDatabaseInModel(): EventEntity {
        return EventEntity(
                date = date,
                description = description,
                image = image,
                longitude = longitude,
                latitude = latitude,
                price = price,
                title = title,
                id = id
        )
    }
}

fun List<Event>.asDatabaseInModel(): Array<EventEntity>{
    return this.map{
        EventEntity(
            date = it.date,
            description = it.description,
            image = it.image,
            longitude = it.longitude,
            latitude = it.latitude,
            price = it.price,
            title = it.title,
            id = it.id
        )
    }.toTypedArray()
}