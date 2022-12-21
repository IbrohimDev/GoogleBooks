package com.example.googlebooks.data.remote.api

import com.example.googlebooks.data.remote.response.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("/books/v1/volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("startIndex") startIndex: Int,
        @Query("key") apiKey: String
    ): Response<BooksResponse>
}