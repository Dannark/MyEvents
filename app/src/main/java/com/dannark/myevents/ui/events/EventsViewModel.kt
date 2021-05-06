package com.dannark.myevents.ui.events

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dannark.myevents.R
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.repository.event.DefaultEventRepository
import com.dannark.myevents.util.isConnectedToInternet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EventsViewModel(private val app: Application) : AndroidViewModel(app) {
    private var viewModelJob = SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private val eventRepository = DefaultEventRepository.getRepository(app)
    val eventList = eventRepository.events

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        loadEvents(false)
    }

    fun loadEvents(isRefresh:Boolean = true){
        val isConnected = isConnectedToInternet(app)

        if (isConnected) {
            _dataLoading.value = true
            uiScope.launch {
                eventRepository.refreshEvents()
                _dataLoading.value = false

                if (isRefresh){
                    Toast.makeText(app, app.getString(R.string.refreshed), Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(app, "No Connection to the internet!", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshEvents(){
        loadEvents(true)
    }
}