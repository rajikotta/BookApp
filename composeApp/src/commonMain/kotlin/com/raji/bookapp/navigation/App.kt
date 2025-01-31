package com.raji.bookapp.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.raji.bookapp.presentation.bookdetail.BookDetailAction
import com.raji.bookapp.presentation.bookdetail.BookDetailViewModel
import com.raji.bookapp.presentation.bookdetail.BookDetailsScreenRoot
import com.raji.bookapp.presentation.booklist.BookListScreenRoot
import com.raji.bookapp.presentation.booklist.BookListViewModel
import com.raji.bookapp.presentation.booklist.SharedViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {


    MaterialTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Routes.BookGraph) {
            navigation<Routes.BookGraph>(startDestination = Routes.BookList) {

                composable<Routes.BookList> { navBackStackEntry ->

                    val sharedViewModel =
                        navBackStackEntry.getSharedKoinViewModel<SharedViewModel>(navController)

                    LaunchedEffect(Unit) {
                        sharedViewModel.onSelectBook(null)
                    }

                    BookListScreenRoot(viewModel = koinViewModel<BookListViewModel>(),
                        onBookClick = { book ->
                            sharedViewModel.onSelectBook(book)
                            navController.navigate(Routes.BookDetails(book.id))
                        })
                }

                composable<Routes.BookDetails> { navBackStackEntry ->

                    val args = navBackStackEntry.toRoute<Routes.BookDetails>()
                    val sharedViewModel =
                        navBackStackEntry.getSharedKoinViewModel<SharedViewModel>(navController)

                    val selecetedBook by sharedViewModel.selectedBook.collectAsStateWithLifecycle()
                    val viewModel = koinViewModel<BookDetailViewModel>()

                    LaunchedEffect(selecetedBook) {
                        selecetedBook?.let {
                            viewModel.onAction(BookDetailAction.OnSelectedBookChange(it))
                        }
                    }


                    BookDetailsScreenRoot(
                        viewModel,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )

                }
            }

        }


    }


}


@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.getSharedKoinViewModel(navController: NavController): T {

    val navGraphRoute = this.destination.parent?.route ?: return koinViewModel<T>()

    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)


}