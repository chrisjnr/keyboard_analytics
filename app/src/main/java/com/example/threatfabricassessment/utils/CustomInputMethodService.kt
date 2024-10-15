package com.example.threatfabricassessment.utils

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.view.inputmethod.InputConnection
import com.example.domain.models.TypingEvent
import com.example.threatfabricassessment.R

class CustomInputMethodService : InputMethodService(), KeyboardView.OnKeyboardActionListener {
    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var keyPressedTime = 0L
    private val excludedKeys = mutableListOf(Keyboard.KEYCODE_DELETE, -1)
    private var isCapsLockOn: Boolean = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val inputConnection: InputConnection? = currentInputConnection
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE, Keyboard.KEYCODE_DONE -> {}
            -1 -> {
                isCapsLockOn = !isCapsLockOn
            }

            else -> {
                var char = primaryCode.toChar()
                if (isCapsLockOn) char = char.uppercaseChar()
                inputConnection?.commitText(char.toString(), 1)
            }
        }
    }

    override fun onPress(primaryCode: Int) {
        keyPressedTime = System.currentTimeMillis()
    }

    override fun onRelease(primaryCode: Int) {
        if (primaryCode !in excludedKeys) {
            val intent = Intent(
                "com.example.threatfabricassessment"
            )
            intent.putExtra(
                "tf_analytics",
                TypingEvent(
                    primaryCode.toChar().toString(),
                    keyPressedTime,
                    System.currentTimeMillis(),
                    applicationContext.resources.configuration.orientation
                )
            )
            sendBroadcast(intent)
        }
    }

    override fun onText(text: CharSequence?) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}
