package com.dannark.myevents.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckInNetwork(
    @Json(name = "event_id")
    val eventId: String,
    val name: String,
    val email: String
)