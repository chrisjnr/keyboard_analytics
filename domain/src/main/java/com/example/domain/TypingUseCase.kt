package com.example.domain

import com.example.domain.models.TypingEvent
import com.example.domain.models.WordEvent
import kotlinx.coroutines.flow.Flow

interface TypingUseCase {
    suspend fun insertTypingEvent(event: TypingEvent)
    suspend fun getTypingEvents(): Flow<List<TypingEvent>>
    suspend fun getEarliestTime(): Flow<Long>
    fun updateWord(username: String, newEndTime: Long, newWord: String, errorCount: Int)
    fun getUpdatedWord(name: String): Flow<WordEvent>
    fun saveWordEvent(word: WordEvent)
}