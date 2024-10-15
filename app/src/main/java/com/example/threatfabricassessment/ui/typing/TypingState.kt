package com.example.threatfabricassessment.ui.typing

import com.example.core.mvi.UiState

data class TypingState(
    val wpmState: Int,
    val adjustedWpmState: Int
): UiState()
