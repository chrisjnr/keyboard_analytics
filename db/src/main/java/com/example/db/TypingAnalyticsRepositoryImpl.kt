package com.example.db

import com.example.db.dao.TypingEventDao
import com.example.db.dao.WordEventDao
import com.example.domain.models.TypingEvent
import com.example.domain.models.WordEvent
import repositories.TypingAnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TypingAnalyticsRepository(
    private val eventDao: TypingEventDao,
    private val wordEventDao: WordEventDao,
    private val mapper: TypingAnalyticsMapper
) : TypingAnalyticsRepository {

    override fun saveTypingEvent(event: TypingEvent) {
        eventDao.insert(mapper.toTypingEventEntity(event))
    }

    override fun getAllTypingEvents(): Flow<List<TypingEvent>> {
        return eventDao.getAllTypingEvents().map {
            it.map { entity ->
                mapper.toTypingEvent(entity)
            }
        }
    }

    override fun saveWordEvent(word: WordEvent) {
        wordEventDao.insert(
            mapper.toTypingWordsEntity(word)
        )
    }

    override fun getEarliestTime(): Flow<Long> {
        return eventDao.getEarliestTime()
    }

    override fun updateWord(username: String, newEndTime: Long, newWord: String, errorCount: Int) {
        wordEventDao.updateWord(username, newEndTime, newWord, errorCount)
    }

    override fun getUpdatedWord(name: String): Flow<WordEvent> {
        return wordEventDao.getUpdatedWord(name).map {
            mapper.toWordEvent(it)
        }
    }
}