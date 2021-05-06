package com.dannark.myevents.repository.event.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.dannark.myevents.database.EventDao
import com.dannark.myevents.database.EventEntity
import com.dannark.myevents.database.asDomainInModel
import com.dannark.myevents.domain.Event
import com.dannark.myevents.domain.asDatabaseInModel
import com.dannark.myevents.network.CheckInNetwork
import com.dannark.myevents.repository.event.EventsDataSource

class EventsLocalDataSource internal constructor(
    val eventDao: EventDao) : EventsDataSource{

    override val events: LiveData<List<Event>> = Transformations.map(eventDao.getAll()){
        it.asDomainInModel()
    }

    override suspend fun getEvents(): List<Event> {
        return eventDao.getAllWithoutObserve().asDomainInModel()
    }

    override suspend fun setEvents(eventList: List<Event>){
        eventDao.insertAll(*eventList.asDatabaseInModel())
    }

    override fun deleteEvents(eventList: Array<EventEntity>){
        eventList?.let {
            eventDao.delete(*eventList)
        }
    }

    override suspend fun postCheckIn(checkInNetwork: CheckInNetwork): String? {
        return null //NO-OP
    }
}