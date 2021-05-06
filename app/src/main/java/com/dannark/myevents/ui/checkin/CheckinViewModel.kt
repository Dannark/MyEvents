package com.dannark.myevents.ui.checkin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.network.CheckInNetwork
import com.dannark.myevents.repository.event.DefaultEventRepository
import kotlinx.coroutines.*
import java.lang.Exception

class CheckinViewModel(app: Application): AndroidViewModel(app) {
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

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    private val eventRepository = DefaultEventRepository.getRepository(app)

    fun postCheckIn(eventId: String, userName: String, lastName: String) {
        _loadingCheckInState.value = true
        uiScope.launch {
            try {
                val checkIn = CheckInNetwork(eventId, userName, lastName)
                _isCheckinSuccessful.value = eventRepository.postCheckIn(checkIn)
            } catch (e: Exception) {
                Log.e("CheckinViewModel","Error when posting Checkin data to server: $e")
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