package com.example.googlebooks.data.remote.response

data class BooksResponse(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)