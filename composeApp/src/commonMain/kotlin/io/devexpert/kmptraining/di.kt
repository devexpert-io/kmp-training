package io.devexpert.kmptraining

import io.devexpert.kmptraining.ui.screens.login.LoginViewModel
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetailViewModel
import io.devexpert.kmptraining.ui.screens.notes.NotesViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::NotesViewModel)
    viewModelOf(::NoteDetailViewModel)
}

fun initKoin(init: KoinApplication.() -> Unit = {}) {
    startKoin {
        init()
        modules(appModule, nativeModule, sharedModule)
    }
}