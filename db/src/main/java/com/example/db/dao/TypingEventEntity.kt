package com.example.db.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "typing_event_table")
data class TypingEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val keyCode: String,
    val keyPressedTime: Long,
    val keyReleasedTime: Long,
    val phoneOrientation: Int,
)