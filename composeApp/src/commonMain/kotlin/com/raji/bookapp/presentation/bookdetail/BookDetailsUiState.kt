package com.raji.bookapp.presentation.bookdetail

import com.raji.bookapp.domain.Book

data class BookDetailsUiState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null


)