package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.dao.TypingEventDao
import com.example.db.dao.TypingEventEntity
import com.example.db.dao.TypingWordsEntity
import com.example.db.dao.WordEventDao

@Database(entities = [TypingEventEntity::class, TypingWordsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun typingEventDao(): TypingEventDao
    abstract fun wordEventDao(): WordEventDao
}