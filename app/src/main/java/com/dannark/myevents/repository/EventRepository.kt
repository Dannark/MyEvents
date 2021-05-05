package com.dannark.myevents.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.database.asDomainInModel
import com.dannark.myevents.domain.CheckIn
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.Network
import com.dannark.myevents.network.asDatabaseInModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception

class EventRepository(private val database: MyEventsDatabase) {
    val events: LiveData<List<Event>> = Transformations.map(database.eventDao.getAll()){
        it.asDomainInModel()
    }

    suspend fun refreshEvents(){
        withContext(Dispatchers.IO){
            try {
                val eventList = Network.events.getEventList().await()
                database.eventDao.insertAll(*eventList.asDatabaseInModel())
            }
            catch (e: Exception){
                Log.e("EventRepository","Couldn't update the list from the server: ${e}")
            }
        }
    }

    suspend fun postCheckIn(checkIn: CheckIn){
        withContext(Dispatchers.IO){
            try {
                Network.events.postCheckIn(checkIn)
            } catch (e: Exception) {
                Log.e("EventRepository","Error when posting Checkin data to server: $e")
            }

        }
    }
}