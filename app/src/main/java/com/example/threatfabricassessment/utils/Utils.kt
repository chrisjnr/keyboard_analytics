package com.example.threatfabricassessment.utils

import android.content.Context
import android.provider.Settings
import android.view.KeyEvent
import android.view.View.OnKeyListener
import android.widget.EditText
import com.example.domain.models.TypingEvent
import com.example.threatfabricassessment.ui.typing.TypingUIEvent


fun Context.isCustomKeyboardActive(customKeyboardPackage: String): Boolean {
    val currentInputMethodId = Settings.Secure.getString(
        contentResolver,
        Settings.Secure.DEFAULT_INPUT_METHOD
    )
    return currentInputMethodId.startsWith(customKeyboardPackage)
}

val keys = mapOf(
    'a' to KeyEvent.KEYCODE_A,
    'b' to KeyEvent.KEYCODE_B,
    'c' to KeyEvent.KEYCODE_C,
    'd' to KeyEvent.KEYCODE_D,
    'e' to KeyEvent.KEYCODE_E,
    'f' to KeyEvent.KEYCODE_F,
    'g' to KeyEvent.KEYCODE_G,
    'h' to KeyEvent.KEYCODE_H,
    'i' to KeyEvent.KEYCODE_I,
    'j' to KeyEvent.KEYCODE_J,
    'k' to KeyEvent.KEYCODE_K,
    'l' to KeyEvent.KEYCODE_L,
    'm' to KeyEvent.KEYCODE_M,
    'n' to KeyEvent.KEYCODE_N,
    'o' to KeyEvent.KEYCODE_O,
    'p' to KeyEvent.KEYCODE_P,
    'q' to KeyEvent.KEYCODE_Q,
    'r' to KeyEvent.KEYCODE_R,
    's' to KeyEvent.KEYCODE_S,
    't' to KeyEvent.KEYCODE_T,
    'u' to KeyEvent.KEYCODE_U,
    'v' to KeyEvent.KEYCODE_V,
    'w' to KeyEvent.KEYCODE_W,
    'x' to KeyEvent.KEYCODE_X,
    'y' to KeyEvent.KEYCODE_Y,
    'z' to KeyEvent.KEYCODE_Z,
    '0' to KeyEvent.KEYCODE_0,
    '1' to KeyEvent.KEYCODE_1,
    '2' to KeyEvent.KEYCODE_2,
    '3' to KeyEvent.KEYCODE_3,
    '4' to KeyEvent.KEYCODE_4,
    '5' to KeyEvent.KEYCODE_5,
    '6' to KeyEvent.KEYCODE_6,
    '7' to KeyEvent.KEYCODE_7,
    '8' to KeyEvent.KEYCODE_8,
    '9' to KeyEvent.KEYCODE_9,
    ' ' to KeyEvent.KEYCODE_SPACE,
    '.' to KeyEvent.KEYCODE_PERIOD,
    ',' to KeyEvent.KEYCODE_COMMA,
    '/' to KeyEvent.KEYCODE_SLASH,
)

fun String.getKeyCode(): Int {
    return keys[this.lowercase().first()]?: KeyEvent.KEYCODE_UNKNOWN
}


interface CustomKeyListener {
    fun onTypingEvent(typingEvent: TypingUIEvent.CollectAnalytics)
    fun isHardWareKeyboard(fromHardware: Boolean)
}

fun EditText.setCustomOnKeyListener(listener: CustomKeyListener) {
    var capsLock = false
    var keydownTime = 0L
    val customListener = OnKeyListener { _, keyCode, event ->
        listener.isHardWareKeyboard(true)
        val invalidKeys = mutableListOf(KeyEvent.KEYCODE_DEL, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_FORWARD, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BACK)
        val capsKeys = listOf(KeyEvent.KEYCODE_CAPS_LOCK, KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_SHIFT_RIGHT)
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode in capsKeys) {
                capsLock = true
            }
            keydownTime = System.currentTimeMillis()
        }
        if (event.action == KeyEvent.ACTION_UP && event.keyCode !in invalidKeys && event.displayLabel.isLetterOrDigit() ) {
            val keyupTime = System.currentTimeMillis()
            val typingEvent = TypingUIEvent.CollectAnalytics(
                TypingEvent(
                    keyCode = if(capsLock) event.displayLabel.toString() else event.displayLabel.toString().lowercase(),
                    keyPressedTime = keydownTime,
                    keyReleasedTime = keyupTime,
                    resources.configuration.orientation
                )
            )
            listener.onTypingEvent(typingEvent)
            listener.isHardWareKeyboard(false)
            capsLock = false
        }
        (keyCode in invalidKeys)
    }
    setOnKeyListener(customListener)
}
