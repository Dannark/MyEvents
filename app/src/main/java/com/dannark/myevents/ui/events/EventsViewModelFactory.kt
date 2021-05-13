package com.dannark.myevents.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannark.myevents.repository.event.EventRepository

class EventsViewModelFactory(
    private val eventRepository: EventRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventsViewModel::class.java)){
            return EventsViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}