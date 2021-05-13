package com.dannark.myevents.network

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.dannark.myevents.BuildConfig
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.Tls12SocketFactory.Companion.enableTls12
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.InputStream
import java.util.concurrent.TimeUnit


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

private val okHttpClient = OkHttpClient.Builder()
    .enableTls12()
    .build()

object Network{

    private val retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.PRD_URL}/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val events = retrofit.create(MyEventsServices::class.java)
}

@GlideModule
class QuizletGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
    }

    override fun isManifestParsingEnabled() = false
}