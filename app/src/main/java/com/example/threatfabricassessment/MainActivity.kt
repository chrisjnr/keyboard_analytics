package com.example.threatfabricassessment

import android.os.Bundle
import android.view.ActionMode
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActionModeStarted(mode: ActionMode) {
        mode.menu.clear()
        super.onActionModeStarted(mode)
    }
}

