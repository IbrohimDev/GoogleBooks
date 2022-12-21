package com.example.googlebooks.domain.repository.implementation

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.googlebooks.data.local.dao.StarDao
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.remote.api.BooksApi
import com.example.googlebooks.data.remote.dataSource.BooksDataSource
import com.example.googlebooks.data.remote.dataSource.BooksDataSourceOffline
import com.example.googlebooks.data.remote.response.Item
import com.example.googlebooks.domain.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class AppRepositoryImplementation @Inject constructor(
    private val booksApi: BooksApi,
    private val config: PagingConfig,
    private val starDao: StarDao,
    private val context:Context
) : AppRepository {

    override fun searchedItems(searchText: String, scope: CoroutineScope): Flow<PagingData<Item>> =
        Pager(config) {
            Timber.tag("HHH").d("Repository in searchedItems")
            BooksDataSource(context,booksApi, searchText, starDao)
        }.flow.cachedIn(scope)

    override fun itemsFromLocalDb(scope: CoroutineScope): Flow<PagingData<StarEntity>> =
        Pager(config) {
            BooksDataSourceOffline(starDao)
        }.flow.cachedIn(scope)

    override fun insertStarToDb(starEntity: StarEntity) {
        starDao.insertStar(starEntity)
    }

    override fun deleteStarFromDb(bookId: String) {
        starDao.deleteStar(bookId)
    }

    override fun starCount(): Flow<List<StarEntity>> = starDao.countStar()
}