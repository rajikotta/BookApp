package com.raji.bookapp.presentation.booklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bookapp.composeapp.generated.resources.Res
import bookapp.composeapp.generated.resources.favorites
import bookapp.composeapp.generated.resources.no_favorite_results
import bookapp.composeapp.generated.resources.no_search_results
import bookapp.composeapp.generated.resources.search_results
import com.raji.bookapp.domain.Book
import com.raji.bookapp.presentation.DarkBlue
import com.raji.bookapp.presentation.DesertWhite
import com.raji.bookapp.presentation.SandYellow
import com.raji.bookapp.presentation.booklist.components.BookList
import com.raji.bookapp.presentation.booklist.components.BookSearchBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookListScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: BookListViewModel = koinViewModel<BookListViewModel>(),
    onBookClick: (Book) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    BookListScreen(state = state) { action ->
        when (action) {
            is BookListAction.OnBookClick -> onBookClick(action.book)
            else -> Unit
        }
        viewModel.onAction(action)
    }

}


@Composable
fun BookListScreen(
    state: BookListUiState, onAction: (BookListAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState { 2 }

    val searchResultScrollState = rememberLazyListState()
    val favoriteListScrollState = rememberLazyListState()

    LaunchedEffect(state.searchResults) {
        searchResultScrollState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(BookListAction.OnTabSelected(pagerState.currentPage))

    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = DarkBlue).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = { onAction(BookListAction.OnSearchQueryChange(it)) },
            onImeAction = { keyboardController?.hide() },
            modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth().padding(16.dp)
        )

        Surface(
            modifier = Modifier.fillMaxSize(), color = DesertWhite, shape = RoundedCornerShape(
                topStart = 32.dp, topEnd = 32.dp
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    containerColor = DesertWhite,
                    modifier = Modifier.padding(vertical = 12.dp).widthIn(700.dp).fillMaxWidth(),
                    indicator = { tabPosition ->
                        TabRowDefaults.SecondaryIndicator(
                            color = SandYellow,
                            modifier = Modifier.tabIndicatorOffset(
                                tabPosition[state.selectedTabIndex]
                            )
                        )
                    }
                ) {
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(0))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = .5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_results),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(1))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = .5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.favorites),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) { pageIndex: Int ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when (pageIndex) {
                            0 -> {
                                if (state.isLoading) {
                                    CircularProgressIndicator()
                                } else {
                                    when {
                                        state.errorMessage != null -> Text(
                                            text = state.errorMessage.asString(),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )

                                        state.searchResults.isEmpty() -> Text(
                                            text = stringResource(Res.string.no_search_results),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )

                                        else -> BookList(
                                            books = state.searchResults,
                                            onBookClick = {
                                                onAction(BookListAction.OnBookClick(it))
                                            }, modifier = Modifier.fillMaxSize(),
                                            scrollState = searchResultScrollState
                                        )
                                    }

                                }

                            }

                            1 -> {
                                if (state.favoriteBooks.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.no_favorite_results),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                    )
                                } else {
                                    BookList(
                                        books = state.favoriteBooks,
                                        onBookClick = {
                                            onAction(BookListAction.OnBookClick(it))
                                        }, modifier = Modifier.fillMaxSize(),
                                        scrollState = favoriteListScrollState
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}