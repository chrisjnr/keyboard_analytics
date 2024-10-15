package com.example.db

import com.example.db.dao.TypingEventEntity
import com.example.db.dao.TypingWordsEntity
import com.example.domain.models.TypingEvent
import com.example.domain.models.WordEvent

class TypingAnalyticsMapper {

    fun toTypingEventEntity(event: TypingEvent): TypingEventEntity {
        return TypingEventEntity(
            keyCode = event.keyCode,
            keyPressedTime = event.keyPressedTime,
            keyReleasedTime = event.keyReleasedTime,
            phoneOrientation = event.orientation,
        )
    }

    fun toTypingEvent(domain: TypingEventEntity): TypingEvent {
        return TypingEvent(
            keyCode = domain.keyCode,
            keyPressedTime = domain.keyPressedTime,
            keyReleasedTime = domain.keyReleasedTime,
            orientation = domain.phoneOrientation,
        )
    }

    fun toTypingWordsEntity(word: WordEvent): TypingWordsEntity {
        return TypingWordsEntity(
            words = word.words,
            username = word.username,
            startTime = word.startTime,
            endTime = word.endTime,
            errorCount = word.errorCount
        )
    }

    fun toWordEvent(domain: TypingWordsEntity): WordEvent {
        return WordEvent(
            words = domain.words,
            username = domain.username,
            startTime = domain.startTime,
            endTime = domain.endTime,
            errorCount = domain.errorCount
        )
    }
}
