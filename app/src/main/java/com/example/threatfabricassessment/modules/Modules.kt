package com.example.threatfabricassessment.modules

import com.example.domain.TypingUseCase
import com.example.domain.TypingUseCaseImpl
import com.example.threatfabricassessment.ui.typing.TypingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel {
        TypingViewModel(get())
    }
}

val domainModule = module {
    single<TypingUseCase> {
        TypingUseCaseImpl(get())
    }
}


