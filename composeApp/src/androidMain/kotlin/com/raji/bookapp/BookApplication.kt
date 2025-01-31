package com.raji.bookapp

import android.app.Application
import com.raji.bookapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class BookApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@BookApplication)
        }
    }
}