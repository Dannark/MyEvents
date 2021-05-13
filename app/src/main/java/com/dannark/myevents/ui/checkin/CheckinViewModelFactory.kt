package com.dannark.myevents.ui.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannark.myevents.domain.Event
import com.dannark.myevents.repository.event.EventRepository
import com.dannark.myevents.ui.eventdetails.EventDetailViewModel

class CheckinViewModelFactory (
    private val eventRepository: EventRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CheckinViewModel::class.java)){
            return CheckinViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}