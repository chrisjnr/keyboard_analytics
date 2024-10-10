package com.example.threatfabricassessment.ui.typing

import com.example.core.mvi.UiEvent

sealed class TypingEvent: UiEvent() {
    data class TextTyped(val text: String): TypingEvent()
}