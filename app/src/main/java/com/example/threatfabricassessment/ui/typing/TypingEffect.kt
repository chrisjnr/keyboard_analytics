package com.example.threatfabricassessment.ui.typing

import com.example.core.mvi.UiEffect

sealed class TypingEffect: UiEffect() {
    data class UpdateReferenceText(val errorPositions: List<Int>): TypingEffect()
}