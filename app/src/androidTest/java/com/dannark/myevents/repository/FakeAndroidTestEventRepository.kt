package com.dannark.myevents.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.CheckInNetwork
import com.dannark.myevents.repository.event.EventRepository
import kotlinx.coroutines.runBlocking
import java.util.LinkedHashMap

class FakeAndroidTestEventRepository: EventRepository {

    var eventServiceData: LinkedHashMap<String, Event> = LinkedHashMap()

    private val _event = MutableLiveData<List<Event>>()
    override val events: LiveData<List<Event>> get() = _event

    override suspend fun refreshEvents() {
        _event.value = eventServiceData.values.toList()
    }

    override suspend fun postCheckIn(checkInNetwork: CheckInNetwork): Boolean {
        return true
    }

    override fun findAndDeleteOldEvents(eventList: List<Event>) {
        //NO-OP
    }

    fun addEventsHelper(vararg events: Event) {
        for (event in events) {
            eventServiceData[event.id] = event
        }
    }
}