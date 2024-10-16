package com.example.threatfabricassessment.ui.typing

import android.view.KeyEvent
import com.example.threatfabricassessment.utils.getKeyCode
import org.junit.Assert.assertEquals
import org.junit.Test

class KeyCodeMapperTest {

    private val testCases = mapOf(
        "a" to KeyEvent.KEYCODE_A,
        "b" to KeyEvent.KEYCODE_B,
        "c" to KeyEvent.KEYCODE_C,
        "d" to KeyEvent.KEYCODE_D,
        "e" to KeyEvent.KEYCODE_E,
        "f" to KeyEvent.KEYCODE_F,
        "g" to KeyEvent.KEYCODE_G,
        "h" to KeyEvent.KEYCODE_H,
        "i" to KeyEvent.KEYCODE_I,
        "j" to KeyEvent.KEYCODE_J,
        "k" to KeyEvent.KEYCODE_K,
        "l" to KeyEvent.KEYCODE_L,
        "m" to KeyEvent.KEYCODE_M,
        "n" to KeyEvent.KEYCODE_N,
        "o" to KeyEvent.KEYCODE_O,
        "p" to KeyEvent.KEYCODE_P,
        "q" to KeyEvent.KEYCODE_Q,
        "r" to KeyEvent.KEYCODE_R,
        "s" to KeyEvent.KEYCODE_S,
        "t" to KeyEvent.KEYCODE_T,
        "u" to KeyEvent.KEYCODE_U,
        "v" to KeyEvent.KEYCODE_V,
        "w" to KeyEvent.KEYCODE_W,
        "x" to KeyEvent.KEYCODE_X,
        "y" to KeyEvent.KEYCODE_Y,
        "z" to KeyEvent.KEYCODE_Z,
        "0" to KeyEvent.KEYCODE_0,
        "1" to KeyEvent.KEYCODE_1,
        "2" to KeyEvent.KEYCODE_2,
        "3" to KeyEvent.KEYCODE_3,
        "4" to KeyEvent.KEYCODE_4,
        "5" to KeyEvent.KEYCODE_5,
        "6" to KeyEvent.KEYCODE_6,
        "7" to KeyEvent.KEYCODE_7,
        "8" to KeyEvent.KEYCODE_8,
        "9" to KeyEvent.KEYCODE_9,
        " " to KeyEvent.KEYCODE_SPACE,
        "." to KeyEvent.KEYCODE_PERIOD,
        "," to KeyEvent.KEYCODE_COMMA,
        "/" to KeyEvent.KEYCODE_SLASH,
        "|" to KeyEvent.KEYCODE_UNKNOWN
    )

    @Test
    fun testGetKeyCode() {
        for ((input, expectedKeyCode) in testCases) {
            val result = input.getKeyCode()
            assertEquals("KeyCode for '$input' should be $expectedKeyCode", expectedKeyCode, result)
        }
    }
}
