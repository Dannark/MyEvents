package com.dannark.myevents.ui.checkin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.domain.CheckIn
import com.dannark.myevents.repository.EventRepository
import kotlinx.coroutines.*

class CheckinViewModel(app: Application): AndroidViewModel(app) {
    private var viewModelJob = SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _loadingCheckInState: MutableLiveData<Boolean> = MutableLiveData()
    val loadingCheckInState: LiveData<Boolean>
        get() = _loadingCheckInState

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    private val database = MyEventsDatabase.getInstance(app)

    private val eventRepository = EventRepository(database)

    fun postCheckIn(eventId: String, userName: String, lastName: String) {

        _loadingCheckInState.value = true
        uiScope.launch {
            val checkIn = CheckIn(eventId, userName, lastName)
            eventRepository.postCheckIn(checkIn)
            _loadingCheckInState.value = false
        }
    }

    fun isNameFieldValid(text: String): Boolean{
        return text.isNotBlank() && text.length > 5 && text.isNotEmpty()
    }

    fun isValidEmailField(text: String): Boolean{
        return text.isNotBlank() && text.length > 5 && text.contains("@") && text.contains(".")
    }
}