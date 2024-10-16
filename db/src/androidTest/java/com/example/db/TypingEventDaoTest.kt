package com.example.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.db.dao.TypingEventDao
import com.example.db.dao.TypingEventEntity
import com.example.db.dao.TypingWordsEntity
import com.example.db.dao.WordEventDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TypingEventDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var typingEventDao: TypingEventDao
    private lateinit var wordEventDao: WordEventDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        typingEventDao = db.typingEventDao()
        wordEventDao = db.wordEventDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testMethod1() = runTest {
        val typingEvent = TypingEventEntity(
            keyCode = "A",
            keyPressedTime = 1000L,
            keyReleasedTime = 1500L,
            phoneOrientation = 0
        )
        typingEventDao.insert(typingEvent)

        val allEvents = typingEventDao.getAllTypingEvents().first()
        assertEquals(1, allEvents.size)
        assertEquals("A", allEvents[0].keyCode)
    }

    @Test
    fun updateWordEvent() = runTest {
        val wordEvent = TypingWordsEntity(
            words = "He thought he would",
            username = "user1",
            startTime = 1000L,
            endTime = 2000L,
            errorCount = 1
        )
        wordEventDao.insert(wordEvent)

        // Update the word event
        wordEventDao.updateWord("user1", 3000L, " light the", 2)

        val updatedWord = wordEventDao.getUpdatedWord("user1").first()
        assertEquals("He thought he would light the", updatedWord.words)
        assertEquals(2, updatedWord.errorCount)
        assertEquals(3000L, updatedWord.endTime)
    }

    @Test
    fun getEarliestTime() = runTest {
        val typingEvent1 = TypingEventEntity(
            keyCode = "B",
            keyPressedTime = 1000L,
            keyReleasedTime = 1500L,
            phoneOrientation = 0
        )
        val typingEvent2 = TypingEventEntity(
            keyCode = "C",
            keyPressedTime = 500L,
            keyReleasedTime = 700L,
            phoneOrientation = 0
        )
        typingEventDao.insert(typingEvent1)
        typingEventDao.insert(typingEvent2)

        val earliestTime = typingEventDao.getEarliestTime().first()
        assertEquals(500L, earliestTime)
    }
}
