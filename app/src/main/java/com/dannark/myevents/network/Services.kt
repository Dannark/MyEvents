package com.dannark.myevents.network

import com.dannark.myevents.BuildConfig
import com.dannark.myevents.domain.Event
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface MyEventsServices {

    @POST("checkin")
    fun postCheckIn(@Body checkInNetwork: CheckInNetwork): Call<NetworkResponse>

    @GET("events")
    fun getEventList():
            Deferred<List<EventNetwork>>

    @GET("events/{id}")
    fun getEvent(@Path("id") id: String):
            Deferred<List<Event>>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network{
    private val retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.SERVER_URL}/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val events = retrofit.create(MyEventsServices::class.java)
}