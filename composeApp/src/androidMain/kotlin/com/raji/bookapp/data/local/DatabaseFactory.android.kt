package com.raji.bookapp.data.local

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseFactory(private val context: Context) {
    actual fun create(): RoomDatabase.Builder<BookDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(BookDatabase.DB_NAME)
        return Room.databaseBuilder<BookDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).setQueryCallback(
            { sqlQuery, bindArgs ->
                Log.d("RoomQuery", "SQL Query: $sqlQuery, Args: $bindArgs")
            },
            Executors.newSingleThreadExecutor() // Ensure logging happens on a separate thread
        )
    }


}