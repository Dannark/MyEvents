package com.dannark.myevents.ui.eventdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dannark.myevents.domain.Event

class EventDetailViewModel(event: Event, application:Application): AndroidViewModel(application) {
    private val _selectedEvent = MutableLiveData<Event>()
    val selectedEvent: LiveData<Event>
        get() = _selectedEvent

    private val _onCheckinNavigationEvent = MutableLiveData<Boolean>()
    val onCheckinNavigationEvent: LiveData<Boolean>
            get() = _onCheckinNavigationEvent

    init {
        _selectedEvent.value = event
    }

    fun openCheckInDialog(){
        _onCheckinNavigationEvent.value = true
    }

    fun onCheckinNavigationCompleted(){
        _onCheckinNavigationEvent.value = false
    }
}