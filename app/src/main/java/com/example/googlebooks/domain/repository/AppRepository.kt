package com.example.googlebooks.domain.repository

import androidx.paging.PagingData
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.remote.response.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface AppRepository {


    fun searchedItems(
        searchText: String,
        scope: CoroutineScope
    ): Flow<PagingData<Item>>

    fun itemsFromLocalDb(
        scope: CoroutineScope
    ): Flow<PagingData<StarEntity>>

    fun insertStarToDb(starEntity: StarEntity)

    fun deleteStarFromDb(bookId: String)

    fun starCount(): Flow<List<StarEntity>>
}