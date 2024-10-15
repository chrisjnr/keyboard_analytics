package repositories

import com.example.domain.models.TypingEvent
import com.example.domain.models.WordEvent
import kotlinx.coroutines.flow.Flow

interface TypingAnalyticsRepository {
    fun saveTypingEvent(event: TypingEvent)
    fun getAllTypingEvents(): Flow<List<TypingEvent>>
    fun getEarliestTime(): Flow<Long>
    fun updateWord(username: String, newEndTime: Long, newWord: String, errorCount: Int)
    fun getUpdatedWord(name: String): Flow<WordEvent>
    fun saveWordEvent(word: WordEvent)
}