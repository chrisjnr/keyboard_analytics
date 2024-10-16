package com.example.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.db.dao.TypingWordsEntity
import com.example.db.dao.WordEventDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WordEventDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var wordEventDao: WordEventDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        wordEventDao = database.wordEventDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetWordEvent() = runTest {
        val wordEvent = TypingWordsEntity(
            id = 1,
            words = "hello",
            username = "user1",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis(),
            errorCount = 2
        )

        wordEventDao.insert(wordEvent)

        val retrievedWordEvent = wordEventDao.getWord("user1").first()

        assertEquals("hello", retrievedWordEvent.words)
        assertEquals("user1", retrievedWordEvent.username)
        assertEquals(2, retrievedWordEvent.errorCount)
    }

    @Test
    fun testUpdateWordEvent() = runTest {
        val wordEvent = TypingWordsEntity(
            id = 1,
            words = "hello",
            username = "user1",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis(),
            errorCount = 0
        )

        wordEventDao.insert(wordEvent)

        wordEventDao.updateWord("user1", System.currentTimeMillis(), " world", 1)

        val updatedWordEvent = wordEventDao.getWord("user1").first()

        assertEquals("hello world", updatedWordEvent.words)
        assertEquals(1, updatedWordEvent.errorCount)
    }

    @Test
    fun testGetAllTypingEvents() = runTest {
        val wordEvent1 = TypingWordsEntity(
            id = 1,
            words = "hello",
            username = "user1",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis(),
            errorCount = 1
        )
        val wordEvent2 = TypingWordsEntity(
            id = 2,
            words = "world",
            username = "user2",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis(),
            errorCount = 0
        )

        wordEventDao.insert(wordEvent1)
        wordEventDao.insert(wordEvent2)

        val allEvents = wordEventDao.getAllTypingEvents().first()

        assertEquals(2, allEvents.size)
    }
}
