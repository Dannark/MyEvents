package com.dannark.myevents.ui.eventdetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannark.myevents.domain.Event

class EventDetailViewModelFactory (
        private val selectedEvent: Event,
        private val application: Application): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventDetailViewModel::class.java)){
            return EventDetailViewModel(selectedEvent, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}