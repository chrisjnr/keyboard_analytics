package com.example.db.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "typing_words_table")
data class TypingWordsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val words: String,
    val username: String,
    val startTime: Long,
    val endTime: Long,
    val errorCount: Int
)
