package com.dannark.myevents.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dannark.myevents.domain.Event

@Entity(tableName = "events")
data class EventEntity (
    val date: Long?,
    val description: String?,
    val image: String?,
    val longitude: String?,
    val latitude: String?,
    val price: Long?,
    val title: String?,

    @PrimaryKey
    val id: String,
)

fun List<EventEntity>.asDomainInModel(): List<Event>{
    return map{
        Event(
            date = it.date,
            description = it.description,
            image = it.image,
            longitude = it.longitude,
            latitude = it.latitude,
            price = it.price,
            title = it.title,
            id = it.id
        )
    }
}