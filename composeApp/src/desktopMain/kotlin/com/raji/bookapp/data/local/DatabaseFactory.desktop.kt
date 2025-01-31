package com.raji.bookapp.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<BookDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), BookDatabase.DB_NAME)
        return Room.databaseBuilder<BookDatabase>(
            name = dbFile.absolutePath,
        )
    }
}