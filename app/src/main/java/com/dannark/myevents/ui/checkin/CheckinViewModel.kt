package com.dannark.myevents.ui.checkin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dannark.myevents.MyEventApp
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.CheckInNetwork
import com.dannark.myevents.repository.event.DefaultEventRepository
import com.dannark.myevents.repository.event.EventRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class CheckinViewModel(private val eventRepository: EventRepository): ViewModel() {
    private var viewModelJob = SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _loadingCheckInState: MutableLiveData<Boolean> = MutableLiveData()
    val loadingCheckInState: LiveData<Boolean>
        get() = _loadingCheckInState

    private val _isCheckinSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    val isCheckinSuccessful: LiveData<Boolean>
        get() = _isCheckinSuccessful

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun postCheckIn(eventId: String, userName: String, email: String) {
        _loadingCheckInState.value = true
        uiScope.launch {
            try {
                val checkIn = CheckInNetwork(eventId, userName, email)
                _isCheckinSuccessful.value = eventRepository.postCheckIn(checkIn)
            } catch (e: Exception) {
                Timber.e("Error when posting Checkin data to server: $e")
            }
            _loadingCheckInState.value = false
        }
    }

    fun isNameFieldValid(text: String): Boolean{
        return text.isNotBlank() && text.length > 5 && text.isNotEmpty()
    }

    fun isValidEmailField(text: String): Boolean{
        return text.isNotBlank() && text.length > 5 && text.contains("@")
                && text.contains(".") && !('.' in "${text.first()}${text.last()}")
    }
}