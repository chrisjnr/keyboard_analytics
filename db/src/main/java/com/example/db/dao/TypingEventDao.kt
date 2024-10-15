package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TypingEventDao {
    @Insert
    fun insert(event: TypingEventEntity)

    @Query("SELECT * FROM typing_event_table")
    fun getAllTypingEvents(): Flow<List<TypingEventEntity>>

    @Query("SELECT MIN(keyPressedTime) AS earliest_time FROM typing_event_table;")
    fun getEarliestTime():Flow<Long>
}