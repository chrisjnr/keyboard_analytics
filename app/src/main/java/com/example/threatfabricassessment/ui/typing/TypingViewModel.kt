package com.example.threatfabricassessment.ui.typing

import com.booking.tripsassignment.core.BaseViewModel

class TypingViewModel: BaseViewModel<TypingState, TypingEffect, TypingEvent>() {
    private var referenceText = ""
    override fun createInitialState(): TypingState {
        return TypingState.Idle
    }

    override fun handleEvent(event: TypingEvent) {
        when(event){
            is TypingEvent.TextTyped -> {
                compareText(event.text)
            }
        }
    }

    fun compareText(userInput: String) {
        val errors = mutableListOf<Int>()
        val inputLength = userInput.length

        for (i in 0 until inputLength) {
            if (i < referenceText.length && userInput[i] != referenceText[i]) {
                errors.add(i)
            }
        }
        sendEffect(TypingEffect.UpdateReferenceText(errors))
    }

    fun setReferenceText(text: String) {
        referenceText = text
    }
}