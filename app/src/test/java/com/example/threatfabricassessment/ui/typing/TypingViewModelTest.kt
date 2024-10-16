package com.example.threatfabricassessment.ui.typing

import app.cash.turbine.test
import com.example.domain.TypingUseCase
import com.example.domain.models.TypingEvent
import com.example.domain.models.WordEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)

class TypingViewModelTest {

    private lateinit var typingViewModel: TypingViewModel
    private lateinit var typingUseCase: TypingUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        typingUseCase = mockk(relaxed = true)
        typingViewModel = TypingViewModel(typingUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `compareText should return error count and trigger UpdateReferenceText effect`() = runTest {
        typingViewModel.setReferenceText("reference")
        val userInput = "referecce"

        val errorCount = typingViewModel.compareText(userInput)

        assertEquals(1, errorCount) // 1 mistake in input

        typingViewModel.effect.test {
            val effect = awaitItem()
            assertEquals(
                effect,
                TypingEffect.UpdateReferenceText(mutableListOf(6))
            )
        }
    }

    @Test
    fun `calculateWPM should update state correctly based on wordEvent`() = runTest {
        val wordEvent = WordEvent(
            words = "hello world",
            username = "TestUser",
            startTime = 1000L,
            endTime = 61000L, // 60 seconds later
            errorCount = 2
        )

        typingViewModel.calculateWPM(wordEvent)

        val expectedWPM = 2
        val expectedAdjWPM = 1

        typingViewModel.state.test {
            val state = awaitItem()
            assertEquals(
                TypingState(wpmState = expectedWPM, adjustedWpmState = expectedAdjWPM),
                state
            )
        }
    }

    @Test
    fun `handleEvent should handle CollectAnalytics emit the event  the flow without calling updateWord`() =
        runTest {
            val typingEvent = TypingEvent("k", 1000L, 1050L, 1)
            val collectAnalyticsEvent = TypingUIEvent.CollectAnalytics(typingEvent)

            typingViewModel.setUserName("username")
            typingViewModel.handleEvent(collectAnalyticsEvent)

            coVerify(inverse = true) {
                typingUseCase.updateWord(
                    "username",
                    typingEvent.keyPressedTime,
                    typingEvent.keyCode,
                    0
                )
            }
            coVerify {
                typingUseCase.saveWordEvent(
                    WordEvent(
                        words = "k",
                        username = "username",
                        startTime = 1000,
                        endTime = 1000,
                        errorCount = 0
                    )
                )
            }

            coVerify {
                typingUseCase.insertTypingEvent(
                    TypingEvent(
                        keyCode = "39",
                        keyPressedTime = 1000,
                        keyReleasedTime = 1050,
                        orientation = 1
                    )
                )
            }

            coVerify {
                typingUseCase.getUpdatedWord("username")
            }
        }

    @Test
    fun `handleEvent should handle CollectAnalytics and emit the event to the flow but only update the typing_word table from the second letter`() =
        runTest {
            var typingEvent = TypingEvent("k", 1000L, 1050L, 1)
            var collectAnalyticsEvent = TypingUIEvent.CollectAnalytics(typingEvent)

            typingViewModel.setUserName("username")
            typingViewModel.handleEvent(collectAnalyticsEvent)

            coVerify(inverse = true) {
                typingUseCase.updateWord(
                    "username",
                    typingEvent.keyPressedTime,
                    typingEvent.keyCode,
                    0
                )
            }

            coVerify {
                typingUseCase.saveWordEvent(
                    WordEvent(
                        words = "k",
                        username = "username",
                        startTime = 1000,
                        endTime = 1000,
                        errorCount = 0
                    )
                )
            }

            coVerify {
                typingUseCase.insertTypingEvent(
                    TypingEvent(
                        keyCode = "39",
                        keyPressedTime = 1000,
                        keyReleasedTime = 1050,
                        orientation = 1
                    )
                )
            }

            coVerify {
                typingUseCase.getUpdatedWord("username")
            }

            typingEvent = TypingEvent("l", 2000L, 4050L, 1)
            collectAnalyticsEvent = TypingUIEvent.CollectAnalytics(typingEvent)
            typingViewModel.handleEvent(collectAnalyticsEvent)

            coVerify {
                typingUseCase.updateWord(
                    "username",
                    2000L,
                    "l",
                    0
                )
            }
        }

    @Test
    fun `observeDbEvents should collect word events and call calculateWPM`() = runTest {
        // Given
        val userName = "TestUser"
        typingViewModel.setUserName(userName)
        val wordEventFlow = flowOf(WordEvent("test", "TestUser", 1726201344235, 1726201388343, 0))
        coEvery { typingUseCase.getUpdatedWord(userName) } returns wordEventFlow

        typingViewModel.observeDbEvents(userName)

        coVerify { typingUseCase.getUpdatedWord(userName) }

        typingViewModel.state.test {
            val state = awaitItem()
            assertEquals(
                TypingState(wpmState = 1, adjustedWpmState = 1),
                state
            )
        }

    }

    @Test
    fun `observeDbEvents should collect word events ad call calculateWPM`() = runTest {
        val userName = "TestUser"
        val wordEventFlow = flowOf(
            WordEvent(
                "testing is the way to go to calculate accurate wpm",
                "TestUser",
                1726201344235,
                1726201388343,
                7
            )
        )
        coEvery { typingUseCase.getUpdatedWord(userName) } returns wordEventFlow

        typingViewModel.observeDbEvents(userName)

        coVerify { typingUseCase.getUpdatedWord(userName) }

        typingViewModel.state.test {
            val state = awaitItem()
            assertEquals(
                TypingState(wpmState = 13, adjustedWpmState = 11),
                state
            )
        }
    }

    @Test
    fun `observeTypingEvents should insert typing events and update word in db`() = runTest {
        val typingEvent = TypingEvent("a", 1000L, 1050L, 1)
        val collectAnalyticsEvent = TypingUIEvent.CollectAnalytics(typingEvent)
        typingViewModel.setUserName("TestUser")
        val expectedWordEvent = WordEvent(
            words = "a",
            username = "TestUser",
            startTime = 1000L,
            endTime = 1000L,
            errorCount = 0
        )

        typingViewModel.handleEvent(collectAnalyticsEvent)

        coVerify { typingUseCase.insertTypingEvent(any()) }
        coVerify { typingUseCase.saveWordEvent(expectedWordEvent) }
    }

    @Test
    fun `observeDbEvents should collect word events ad call calculateWPM but not calculate if startTime == endTime`() =
        runTest {
            val typingEvent = TypingEvent("a", 1000L, 1000L, 1)
            val collectAnalyticsEvent = TypingUIEvent.CollectAnalytics(typingEvent)
            typingViewModel.setUserName("TestUser")
            val expectedWordEvent = WordEvent(
                words = "a",
                username = "TestUser",
                startTime = 1000L,
                endTime = 1000L,
                errorCount = 0
            )

            typingViewModel.handleEvent(collectAnalyticsEvent)

            coVerify { typingUseCase.insertTypingEvent(any()) }
            coVerify { typingUseCase.saveWordEvent(expectedWordEvent) }
            typingViewModel.state.test {
                val state = awaitItem()
                assertEquals(
                    TypingState(wpmState = 0, adjustedWpmState = 0),
                    state
                )
            }
        }
}