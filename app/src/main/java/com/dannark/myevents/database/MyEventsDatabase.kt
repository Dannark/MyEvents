package com.dannark.myevents.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [EventEntity::class], version = 5, exportSchema = false)
abstract class MyEventsDatabase: RoomDatabase() {
    abstract val eventDao: EventDao

    companion object{
        @Volatile
        private var INSTANCE: MyEventsDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context) : MyEventsDatabase{
            kotlinx.coroutines.internal.synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyEventsDatabase::class.java,
                        "history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}