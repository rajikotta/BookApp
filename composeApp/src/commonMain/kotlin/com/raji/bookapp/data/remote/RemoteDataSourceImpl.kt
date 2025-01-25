package com.raji.bookapp.data.remote

import com.raji.bookapp.data.remote.model.ResponseDto
import com.raji.bookapp.data.remote.network.DataError
import com.raji.bookapp.data.remote.network.Result

class RemoteDataSourceImpl:RemoteDataSource {
    override suspend fun searchBook(
        query: String,
        rateLimit: Int?
    ): Result<ResponseDto, DataError.RemoteError> {
        TODO("Not yet implemented")
    }
}