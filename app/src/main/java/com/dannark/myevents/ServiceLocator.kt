package com.dannark.myevents

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.dannark.myevents.database.MyEventsDatabase
import com.dannark.myevents.repository.event.DefaultEventRepository
import com.dannark.myevents.repository.event.EventRepository
import com.dannark.myevents.repository.event.EventsDataSource
import com.dannark.myevents.repository.event.local.EventsLocalDataSource
import com.dannark.myevents.repository.event.remote.EventsRemoteDataSource

object ServiceLocator {

    private var database: MyEventsDatabase? = null
    @Volatile
    var eventRepository: EventRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    fun proviteEventRepository(context: Context): EventRepository {
        synchronized(this){
            return eventRepository ?: createEventRepository(context)
        }
    }

    private fun createEventRepository(context: Context): EventRepository {
        val newRepo = DefaultEventRepository(EventsRemoteDataSource(), createEventLocalDataSource(context))
        eventRepository = newRepo
        return newRepo
    }

    private fun createEventLocalDataSource(context: Context): EventsDataSource {
        val database = database ?: createDatabase(context)
        return EventsLocalDataSource(database.eventDao)
    }

    private fun createDatabase(context: Context): MyEventsDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            MyEventsDatabase::class.java, "history_database"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository(){
        synchronized(lock){
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            eventRepository = null
        }
    }
}