package com.raji.bookapp.domain

import com.raji.bookapp.data.remote.network.DataError
import com.raji.bookapp.data.remote.network.EmptyResult
import com.raji.bookapp.data.remote.network.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun searchBook(query: String): Result<List<Book>, DataError.RemoteError>

    suspend fun getBookDescription(id: String): Result<String?, DataError>

    fun getFavoriteBooks(): Flow<List<Book>>

    fun isBookFavorite(id: String): Flow<Boolean>

    suspend fun markAsFavorite(book: Book): EmptyResult<DataError.LocalError>

    suspend fun deleteFromFavorites(id: String)

}