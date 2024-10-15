package com.example.domain.models

import java.io.Serializable


data class TypingEvent(
    val keyCode: String,
    val keyPressedTime: Long,
    val keyReleasedTime: Long,
    val orientation: Int,
) : Serializable
