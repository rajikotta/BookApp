package com.raji.bookapp.data.remote

import com.raji.bookapp.data.remote.model.BookWorkDto
import com.raji.bookapp.data.remote.model.ResponseDto
import com.raji.bookapp.data.remote.network.DataError
import com.raji.bookapp.data.remote.network.Result
import com.raji.bookapp.data.remote.network.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteDataSourceImpl(private val htpClient: HttpClient) : RemoteDataSource {
    override suspend fun searchBook(
        query: String,
        rateLimit: Int?
    ): Result<ResponseDto, DataError.RemoteError> {
        return safeCall {
            htpClient.get(urlString = "$BASE_URL/search.json") {
                parameter("q", query)
                parameter("limit", rateLimit)
                parameter("language", "eng")
                parameter(
                    "fields",
                    "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count"
                )
            }
        }
    }


    override suspend fun getBookDetail(id: String): Result<BookWorkDto, DataError.RemoteError> {

        return safeCall {
            htpClient.get(urlString = "$BASE_URL/works/$id.json")
        }
    }
}

const val BASE_URL = "https://openlibrary.org"
