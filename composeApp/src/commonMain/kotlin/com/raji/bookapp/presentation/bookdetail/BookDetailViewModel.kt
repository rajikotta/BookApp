package com.raji.bookapp.presentation.bookdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.raji.bookapp.data.remote.network.onSuccess
import com.raji.bookapp.domain.BookRepository
import com.raji.bookapp.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedBookId = savedStateHandle.toRoute<Routes.BookDetails>().book

    private val _state = MutableStateFlow<BookDetailsUiState>(BookDetailsUiState())

    val state = _state.onStart {
        fetchBookDescription()
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    init {
        observeFavoriteStatus()
    }

    fun onAction(action: BookDetailAction) {
        when (action) {
            is BookDetailAction.OnSelectedBookChange -> {
                _state.update {
                    it.copy(book = action.book)
                }
            }

            is BookDetailAction.OnFavoriteClick -> {

                viewModelScope.launch {

                    if (state.value.isFavorite)
                        bookRepository.deleteFromFavorites(state.value.book?.id!!)
                    else {
                        state.value.book?.let { book ->
                            bookRepository.markAsFavorite(book)
                        }
                    }

                }
            }

            else -> Unit
        }

    }

    private fun fetchBookDescription() {
        viewModelScope.launch {

            bookRepository.getBookDescription(selectedBookId)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            book = it.book?.copy(description = result)
                        )
                    }
                }
        }
    }

    private fun observeFavoriteStatus() {

        viewModelScope.launch {
            bookRepository
                .isBookFavorite(selectedBookId)
                .onEach { isFavorite ->
                    _state.update {
                        it.copy(
                            isFavorite = isFavorite
                        )
                    }
                }.collect()
        }

    }
}