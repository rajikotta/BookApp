package com.raji.bookapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BookEntity::class], version = 1)
@TypeConverters(StringListTypeConverter::class)
abstract class BookDatabase : RoomDatabase() {
    abstract val bookDao: BookDao

    companion object {
        const val DB_NAME = "book_db"
    }
}