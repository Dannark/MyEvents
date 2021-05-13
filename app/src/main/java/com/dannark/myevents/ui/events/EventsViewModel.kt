package com.dannark.myevents.ui.events

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dannark.myevents.R
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.repository.event.DefaultEventRepository
import com.dannark.myevents.repository.event.EventRepository
import com.dannark.myevents.util.isConnectedToInternet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class EventsViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private var viewModelJob = SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val eventList = eventRepository.events

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        loadEvents()
    }

    fun loadEvents(){
        _dataLoading.value = true
        uiScope.launch {
            eventRepository.refreshEvents()
            _dataLoading.value = false
        }
    }
}