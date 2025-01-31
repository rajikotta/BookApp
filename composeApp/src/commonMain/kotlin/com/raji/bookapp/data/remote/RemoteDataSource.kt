package com.raji.bookapp.data.remote

import com.raji.bookapp.data.remote.model.BookWorkDto
import com.raji.bookapp.data.remote.model.ResponseDto
import com.raji.bookapp.data.remote.network.DataError
import com.raji.bookapp.data.remote.network.Result

interface RemoteDataSource {

    suspend fun searchBook(
        query: String,
        rateLimit: Int? = null
    ): Result<ResponseDto, DataError.RemoteError>
    suspend fun getBookDetail(id: String): Result<BookWorkDto, DataError.RemoteError>


}