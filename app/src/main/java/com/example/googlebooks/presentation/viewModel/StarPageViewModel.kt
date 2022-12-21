package com.example.googlebooks.presentation.viewModel

import androidx.paging.PagingData
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.remote.response.Item
import kotlinx.coroutines.flow.Flow

interface StarPageViewModel {

    val booksPagingData: Flow<PagingData<StarEntity>>
    val pagerAdapter: Flow<Unit>
    val animeShow: Flow<Boolean>

    fun requestFromLocalDb()
    fun showAnime()
    fun deleteItem(bookId:String)
}