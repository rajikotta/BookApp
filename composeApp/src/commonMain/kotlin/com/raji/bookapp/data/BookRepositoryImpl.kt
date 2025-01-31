package com.raji.bookapp.data

import androidx.sqlite.SQLiteException
import com.raji.bookapp.data.local.BookDao
import com.raji.bookapp.data.local.toBook
import com.raji.bookapp.data.local.toBookEntity
import com.raji.bookapp.data.remote.RemoteDataSource
import com.raji.bookapp.data.remote.model.toBook
import com.raji.bookapp.data.remote.network.DataError
import com.raji.bookapp.data.remote.network.EmptyResult
import com.raji.bookapp.data.remote.network.map
import com.raji.bookapp.domain.Book
import com.raji.bookapp.domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(val remoteDataSource: RemoteDataSource, val dao: BookDao) :
    BookRepository {

    override suspend fun searchBook(query: String): com.raji.bookapp.data.remote.network.Result<List<Book>, DataError.RemoteError> {

        return remoteDataSource.searchBook(query).map { dto -> dto.results.map { it.toBook() } }
    }

    override suspend fun getBookDescription(id: String): com.raji.bookapp.data.remote.network.Result<String?, DataError> {
        return remoteDataSource.getBookDetail(id).map { it.description }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return dao.getFavoriteBooks().map { bookEntityList ->
            bookEntityList.map { it.toBook() }
        }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return dao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.any { it.id == id }
            }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.LocalError> {
        return try {
            dao.upsert(book.toBookEntity())
            com.raji.bookapp.data.remote.network.Result.Success(Unit)
        } catch (e: SQLiteException) {
            com.raji.bookapp.data.remote.network.Result.NetworkError(DataError.LocalError.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {

        dao.deleteFavoriteBook(id)
    }


}