package com.dannark.myevents.ui.eventdetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannark.myevents.domain.Event

class EventDetailViewModelFactory (
        private val selectedEvent: Event): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventDetailViewModel::class.java)){
            return EventDetailViewModel(selectedEvent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}