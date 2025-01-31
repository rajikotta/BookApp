package com.raji.bookapp

import androidx.compose.ui.window.ComposeUIViewController
import com.raji.bookapp.navigation.App

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) { App() }