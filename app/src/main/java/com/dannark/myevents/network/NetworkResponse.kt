package com.dannark.myevents.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkResponse (
        val code: String
)