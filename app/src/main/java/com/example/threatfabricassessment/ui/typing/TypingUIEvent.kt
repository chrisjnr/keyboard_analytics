package com.example.threatfabricassessment.ui.typing

import com.example.core.mvi.UiEvent
import com.example.domain.models.TypingEvent

sealed class TypingUIEvent: UiEvent() {
    data class TextTyped(val text: String, val time: Long): TypingUIEvent()
    data class CollectAnalytics(val typingEvent: TypingEvent): TypingUIEvent()
}
