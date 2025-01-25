package com.raji.bookapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform