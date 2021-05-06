package com.dannark.myevents.repository.event.remote

import androidx.lifecycle.LiveData
import com.dannark.myevents.database.EventEntity
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.*
import com.dannark.myevents.repository.event.EventsDataSource
import retrofit2.await
import java.lang.Exception

class EventsRemoteDataSource() : EventsDataSource {
    override val events: LiveData<List<Event>>
        get() = TODO("Not yet implemented")

    override suspend fun getEvents(): List<Event>
        = Network.events.getEventList().await().asEvent()

    override suspend fun setEvents(eventList: List<Event>) {
        //NO-OP
    }

    override fun deleteEvents(eventList: Array<EventEntity>) {
        //NO-OP
    }

    override suspend fun postCheckIn(checkInNetwork: CheckInNetwork): String? {
        return try {
            Network.events.postCheckIn(checkInNetwork).await().code
        }
        catch (e: Exception){
            null
        }
    }
}