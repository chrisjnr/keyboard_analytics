package com.example.domain.models

data class WordEvent(
    val words: String,
    val username: String,
    val startTime: Long,
    val endTime: Long,
    val errorCount: Int,
)