package com.raji.bookapp.presentation.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raji.bookapp.data.remote.network.onError
import com.raji.bookapp.data.remote.network.onSuccess
import com.raji.bookapp.domain.Book
import com.raji.bookapp.domain.BookRepository
import com.raji.bookapp.presentation.util.toUIText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(val bookRepository: BookRepository) : ViewModel() {


    private val _state = MutableStateFlow<BookListUiState>(BookListUiState())
    val state = _state.onStart {
        if (cachedbook.isEmpty())
            observeSearchQuery()
        observeFavoriteBooks()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = _state.value
    )


    private var cachedbook = emptyList<Book>()
    private var searchJob: Job? = null
    private var observeFavoriteJob: Job? = null


    fun onAction(action: BookListAction) {
        when (action) {
            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = action.query
                    )
                }
            }

            is BookListAction.OnTabSelected -> {

                _state.update {
                    it.copy(
                        selectedTabIndex = action.index
                    )
                }
            }

            is BookListAction.OnBookClick -> {

            }
        }
    }


    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state.map {
            it.searchQuery
        }.distinctUntilChanged().debounce(500L).onEach { query ->
            when {
                query.isBlank() -> {
                    _state.update {
                        it.copy(
                            errorMessage = null,
                            searchResults = cachedbook,
                        )
                    }
                }

                query.length > 2 -> {
                    searchBooks(query)
                }

            }
        }.launchIn(viewModelScope)
    }


    private fun searchBooks(query: String) {

        searchJob?.cancel()


        searchJob = viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            viewModelScope.launch {
                bookRepository.searchBook(query).onSuccess { searchResults ->
                    _state.update {
                        it.copy(
                            searchResults = searchResults,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }.onError { error ->
                    _state.update {
                        it.copy(
                            searchResults = emptyList(),
                            isLoading = false,
                            errorMessage = error.toUIText()
                        )
                    }
                }
            }
        }
    }

    private fun observeFavoriteBooks() {
        observeFavoriteJob?.cancel()
        observeFavoriteJob = bookRepository
            .getFavoriteBooks()
            .onEach { favoriteBooks ->
                _state.update {
                    it.copy(
                        favoriteBooks = favoriteBooks
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}