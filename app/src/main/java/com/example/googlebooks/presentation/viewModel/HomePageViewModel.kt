package com.example.googlebooks.presentation.viewModel

import androidx.paging.PagingData
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.remote.response.Item
import kotlinx.coroutines.flow.Flow

interface HomePageViewModel {

    val booksPagingData: Flow<PagingData<Item>>
    val pagerAdapter: Flow<Unit>
    val animeShow: Flow<Boolean>
    val rvOpen:Flow<Unit>

    fun searchEditText(text: String)
    fun requestSearch(text: String)
    fun stateSearch(state: Boolean)
    fun insertStarToDb(book: StarEntity)
    fun deleteStarFromDb(bookId: String)
    fun reSearch()

}