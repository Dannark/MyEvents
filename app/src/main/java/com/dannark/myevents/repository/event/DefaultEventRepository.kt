package com.dannark.myevents.repository.event

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.dannark.myevents.database.EventEntity
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.network.CheckInNetwork
import com.dannark.myevents.domain.Event
import com.dannark.myevents.domain.asDatabaseInModel
import com.dannark.myevents.repository.event.local.EventsLocalDataSource
import com.dannark.myevents.repository.event.remote.EventsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class DefaultEventRepository(
        private val eventsRemoteDataSource: EventsDataSource,
        private val eventsLocalDataSource: EventsDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO){

    companion object {
        @Volatile
        private var INSTANCE: DefaultEventRepository? = null

        fun getRepository(app: Application): DefaultEventRepository {
            return INSTANCE ?: synchronized(this) {
                val database =  MyEventsDatabase.getInstance(app)

                DefaultEventRepository(EventsRemoteDataSource(), EventsLocalDataSource(database.eventDao)).also {
                    INSTANCE = it
                }
            }
        }
    }

    val events: LiveData<List<Event>> = eventsLocalDataSource.events

    suspend fun refreshEvents(){
        withContext(ioDispatcher){
            try {
                val eventList = eventsRemoteDataSource.getEvents()
                eventsLocalDataSource.setEvents(eventList)
                findAndDeleteOldEvents(eventList)
            }
            catch (e: Exception){
                Log.e("EventRepository","Couldn't update the list from the server: ${e}")
            }
        }
    }

    suspend fun postCheckIn(checkInNetwork: CheckInNetwork): Boolean{
        var code:String?
        withContext(ioDispatcher){
            code = eventsRemoteDataSource.postCheckIn(checkInNetwork)
        }
        return code == "200"
    }

    fun findAndDeleteOldEvents(eventList: List<Event>){
        events.value?.let {
            val itemsToBeDeleted = findDiff(eventList.asDatabaseInModel(), it.toTypedArray())
            eventsLocalDataSource.deleteEvents(itemsToBeDeleted)
        }
    }

    // Data Structured function to find the missing values between two lists
    // Time complexity of O(n)
    private fun findDiff(arr1: Array<EventEntity>, arr2: Array<Event>): Array<EventEntity>{
        val found = hashMapOf<Int, Int>()
        val missing = mutableListOf<EventEntity>()

        for (item in arr1){
            found[item.id.toInt()] = (found[item.id.toInt()]?:0) + 1
        }

        for (item in arr2){
            val id = item.id.toInt()
            found[id] = (found[id]?:0) - 1
            if(found[id]!! < 0){
                missing.add(item.asDatabaseInModel())
            }
        }
        //Log.e("PostRepository -","missing ${missing.size} elements")
        return missing.toTypedArray()
    }
}