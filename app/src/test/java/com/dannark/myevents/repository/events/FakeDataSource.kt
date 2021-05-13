package com.dannark.myevents.repository.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dannark.myevents.database.EventEntity
import com.dannark.myevents.database.asDomainInModel
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.CheckInNetwork

class FakeDataSource(var eventList: List<Event> = mutableListOf()): EventsDataSource {

    private val _events = MutableLiveData(eventList)
    override val events: LiveData<List<Event>> = _events

    var forceError = false

    override suspend fun getEvents(): List<Event> {
        return eventList
    }

    override fun deleteEvents(eventList: Array<EventEntity>) {
        val newList: MutableList<Event> = _events.value?.toMutableList() ?: mutableListOf()

        eventList?.let {
            val toBeRemoved = it.toList().asDomainInModel()

            for (item in toBeRemoved){
                if (newList.contains(item) == true){
                    newList.remove(item)
                }
            }
            _events.value = newList
        }
    }

    override suspend fun setEvents(eventList: List<Event>) {
        _events.value = eventList
    }

    override suspend fun postCheckIn(checkInNetwork: CheckInNetwork): String? {
        //simulates a fail and sends null in case server is down for example
        if(forceError){
            return null
        }
        return "200"
    }
}