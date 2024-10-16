package com.example.threatfabricassessment.ui.typing

import androidx.lifecycle.viewModelScope
import com.example.core.BaseViewModel
import com.example.domain.TypingUseCase
import com.example.domain.models.WordEvent
import com.example.threatfabricassessment.utils.getKeyCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TypingViewModel(private val typingUseCase: TypingUseCase) :
    BaseViewModel<TypingState, TypingEffect, TypingUIEvent>() {
    private var referenceText = ""
    private var userName = ""
    private var update = false
    private var errorCount = 0

    private val _typingUIEventFlow =
        MutableSharedFlow<TypingUIEvent.CollectAnalytics>(extraBufferCapacity = 1)

    init {
        observeTypingEvents()
    }

    private fun observeTypingEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _typingUIEventFlow
                .buffer()
                .collectLatest { event ->
                    handleTypingEvent(event, errorCount)
                }
        }
    }

    private suspend fun handleTypingEvent(event: TypingUIEvent.CollectAnalytics, noOfErrors: Int) {
        val keyChar = event.typingEvent.keyCode.toCharArray().first()

        if (keyChar.isLetterOrDigit()) {
            if (update) {
                typingUseCase.updateWord(
                    userName,
                    event.typingEvent.keyPressedTime,
                    event.typingEvent.keyCode,
                    errorCount
                )
            } else {
                typingUseCase.saveWordEvent(
                    WordEvent(
                        words = event.typingEvent.keyCode,
                        username = userName,
                        startTime = event.typingEvent.keyPressedTime,
                        endTime = event.typingEvent.keyPressedTime,
                        errorCount = noOfErrors,
                    )
                )
                observeDbEvents(userName)
            }
            update = true

            typingUseCase.insertTypingEvent(
                event.typingEvent.copy(
                    keyCode = event.typingEvent.keyCode.getKeyCode().toString()
                )
            )
        }
    }

    fun observeDbEvents(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            typingUseCase.getUpdatedWord(userName)
                .flowOn(Dispatchers.IO)
                .catch { e ->  }
                .collect { wordEvent ->
                    calculateWPM(wordEvent)
                }
        }
    }

    override fun createInitialState(): TypingState {
        return TypingState(0, 0)
    }

    override fun handleEvent(event: TypingUIEvent) {
        when (event) {
            is TypingUIEvent.TextTyped -> {
                errorCount = compareText(event.text)
            }

            is TypingUIEvent.CollectAnalytics -> {
                viewModelScope.launch {
                    _typingUIEventFlow.emit(event)
                }
            }
        }
    }

    fun compareText(userInput: String): Int {
        val errors = mutableListOf<Int>()
        val inputLength = userInput.length

        for (i in 0 until inputLength) {
            if (i < referenceText.length && userInput[i] != referenceText[i]) {
                errors.add(i)
            }
        }
        sendEffect(TypingEffect.UpdateReferenceText(errors))
        return errors.count()
    }

    fun setReferenceText(text: String) {
        referenceText = text
    }

    fun setUserName(name: String) {
        userName = name
    }

    fun calculateWPM(wordEvent: WordEvent) {
        val timeDifference = wordEvent.endTime - wordEvent.startTime
        if (timeDifference > 0L) {
            val elapsedMinutes = timeDifference / 60_000f

            val wpm = (wordEvent.words.count().toFloat() / 5) / elapsedMinutes

            val adjustedWPM = (wordEvent.words.count() - wordEvent.errorCount).toFloat() / 5 / elapsedMinutes

            setState {
                copy(
                    wpmState = wpm.toInt(),
                    adjustedWpmState = if (adjustedWPM < 1) 0 else adjustedWPM.toInt()
                )
            }
        }
    }
}

