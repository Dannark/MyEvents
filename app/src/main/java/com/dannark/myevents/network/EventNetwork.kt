package com.dannark.myevents.network

import com.dannark.myevents.database.EventEntity
import com.dannark.myevents.domain.Event
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventNetwork (

    @Json(name = "date")
    val date: Long?,

    @Json(name = "description")
    val description: String?,

    @Json(name = "image")
    val image: String?,

    @Json(name = "longitude")
    val longitude: String?,

    @Json(name = "latitude")
    val latitude: String?,

    @Json(name = "price")
    val price: Float?,

    @Json(name = "title")
    val title: String?,

    @Json(name = "id")
    val id: String,
)

fun List<EventNetwork>.asEvent(): List<Event>{
    return this.map{
        Event(
            date = it.date,
            description = it.description,
            image = it.image,
            longitude = it.longitude,
            latitude = it.latitude,
            price = it.price?.toLong(),
            title = it.title,
            id = it.id
        )
    }
}