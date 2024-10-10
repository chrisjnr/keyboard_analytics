package com.example.threatfabricassessment.modules

import com.example.threatfabricassessment.ui.typing.TypingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    viewModel {
        TypingViewModel()
    }
}

