package com.raji.bookapp.data.remote.model

import com.raji.bookapp.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BookDto(
    @SerialName("key") val id: String,
    @SerialName("title") val title: String,
    @SerialName("language") val languages: List<String>? = null,
    @SerialName("cover_i") val coverAlternativeKey: Int? = null,
    @SerialName("author_key") val authorKeys: List<String>? = null,
    @SerialName("author_name") val authorNames: List<String>? = null,
    @SerialName("cover_edition_key") val coverKey: String? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("ratings_average") val ratingsAverage: Double? = null,
    @SerialName("ratings_count") val ratingsCount: Int? = null,
    @SerialName("number_of_pages_median") val numPagesMedian: Int? = null,
    @SerialName("edition_count") val numEditions: Int? = null,
)


fun BookDto.toBook(): Book {
    return Book(
        id = id.substringAfterLast("/"),
        title = title,
        imageUrl = if (coverKey != null) {
            "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
        } else {
            "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
        },
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions ?: 0
    )
}
