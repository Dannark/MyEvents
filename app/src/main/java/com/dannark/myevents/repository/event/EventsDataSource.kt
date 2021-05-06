package com.dannark.myevents.repository.event

import androidx.lifecycle.LiveData
import com.dannark.myevents.database.EventEntity
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.CheckInNetwork
import com.dannark.myevents.network.EventNetwork

interface EventsDataSource {

    val events: LiveData<List<Event>>

    suspend fun getEvents(): List<Event>

    fun deleteEvents(eventList: Array<EventEntity>)

    suspend fun setEvents(eventList: List<Event>)

    suspend fun postCheckIn(checkInNetwork: CheckInNetwork): String?
}