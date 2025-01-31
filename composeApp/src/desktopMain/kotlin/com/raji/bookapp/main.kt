package com.raji.bookapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.raji.bookapp.di.initKoin
import com.raji.bookapp.navigation.App

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "BookApp",
        ) {
            App()
        }
    }
}