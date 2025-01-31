package com.raji.bookapp.presentation.bookdetail

import com.raji.bookapp.domain.Book

sealed interface BookDetailAction {

    data object OnBackClick : BookDetailAction
    data object OnFavoriteClick : BookDetailAction
    data class OnSelectedBookChange(val book: Book) : BookDetailAction
}