package com.dannark.myevents.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg event: EventEntity)

    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAll(): LiveData<List<EventEntity>>

    @Delete
    fun delete(vararg eventEntity: EventEntity): Int
}