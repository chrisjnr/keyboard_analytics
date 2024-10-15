package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface WordEventDao {
    @Insert
    fun insert(event: TypingWordsEntity)


    @Query("""
        SELECT * FROM typing_words_table 
        WHERE username = :username 
        ORDER BY startTime DESC 
        LIMIT 1
    """)
    fun getWord(username: String): Flow<TypingWordsEntity>

    fun getUpdatedWord(name:String) = getWord(name).distinctUntilChanged()

    @Query("""
        UPDATE typing_words_table 
        SET words = words || :newWord, endTime = :newEndTime, errorCount = :errorCount
        WHERE id = (
            SELECT id 
            FROM typing_words_table 
            WHERE username = :username 
            ORDER BY startTime DESC 
            LIMIT 1
        )
    """)
    fun updateWord(username: String, newEndTime: Long, newWord: String, errorCount: Int)

    @Query("SELECT * FROM typing_words_table")
    fun getAllTypingEvents(): Flow<List<TypingWordsEntity>>
}