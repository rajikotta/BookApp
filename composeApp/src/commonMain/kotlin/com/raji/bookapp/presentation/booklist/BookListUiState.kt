package com.raji.bookapp.presentation.booklist

import com.raji.bookapp.domain.Book
import com.raji.bookapp.presentation.util.UiText

data class BookListUiState(
    val searchQuery: String = "Kotlin",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)

