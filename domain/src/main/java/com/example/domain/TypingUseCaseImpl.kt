package com.example.domain

import com.example.domain.models.TypingEvent
import com.example.domain.models.WordEvent
import kotlinx.coroutines.flow.Flow
import repositories.TypingAnalyticsRepository

class TypingUseCaseImpl(private val repository: TypingAnalyticsRepository): TypingUseCase {
    override suspend fun insertTypingEvent(event: TypingEvent) {
        repository.saveTypingEvent(event)
    }

    override suspend fun getTypingEvents(): Flow<List<TypingEvent>> {
        return repository.getAllTypingEvents()
    }

    override suspend fun getEarliestTime(): Flow<Long> {
        return repository.getEarliestTime()
    }

    override fun updateWord(username: String, newEndTime: Long, newWord: String, errorCount: Int) {
        repository.updateWord(username, newEndTime, newWord, errorCount)
    }

    override fun getUpdatedWord(name: String): Flow<WordEvent> {
        return repository.getUpdatedWord(name)
    }

    override fun saveWordEvent(word: WordEvent) {
        repository.saveWordEvent(word)
    }
}