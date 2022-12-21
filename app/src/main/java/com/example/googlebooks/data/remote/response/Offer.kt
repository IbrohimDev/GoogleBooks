package com.example.googlebooks.data.remote.response

data class Offer(
    val finskyOfferType: Long,
    val listPrice: ListPriceX,
    val retailPrice: RetailPrice
)