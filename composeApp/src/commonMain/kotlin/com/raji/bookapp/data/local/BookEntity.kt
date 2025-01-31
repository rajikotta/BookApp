package com.raji.bookapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raji.bookapp.domain.Book


@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val languages: List<String>,
    val authors: List<String>,
    val firstPublishYear: String?,
    val ratingsAverage: Double?,
    val ratingsCount: Int?,
    val numPagesMedian: Int?,
    val numEditions: Int
)


fun Book.toBookEntity() = BookEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    languages = languages,
    authors = authors,
    firstPublishYear = firstPublishYear,
    ratingsAverage = averageRating,
    ratingsCount = ratingCount,
    numPagesMedian = numPages,
    numEditions = numEditions
)

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        languages = languages,
        authors = authors,
        firstPublishYear = firstPublishYear,
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions
    )
}