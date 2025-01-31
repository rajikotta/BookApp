package com.raji.bookapp.presentation.booklist

import com.raji.bookapp.domain.Book

interface BookListAction {
    data class OnSearchQueryChange(val query: String) : BookListAction
    data class OnBookClick(val book: Book) : BookListAction
    data class OnTabSelected(val index: Int) : BookListAction

}