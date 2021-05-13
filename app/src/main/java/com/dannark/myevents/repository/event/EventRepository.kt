package com.dannark.myevents.repository.event

import androidx.lifecycle.LiveData
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.CheckInNetwork

interface EventRepository {
    val events: LiveData<List<Event>>

    suspend fun refreshEvents()
    suspend fun postCheckIn(checkInNetwork: CheckInNetwork): Boolean
    fun findAndDeleteOldEvents(eventList: List<Event>)
}