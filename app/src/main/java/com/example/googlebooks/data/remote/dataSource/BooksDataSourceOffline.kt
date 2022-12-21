package com.example.googlebooks.data.remote.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.googlebooks.data.local.dao.StarDao
import com.example.googlebooks.data.local.entity.StarEntity
import javax.inject.Inject

class BooksDataSourceOffline @Inject constructor(
    private val starDao: StarDao
) : PagingSource<Int, StarEntity>() {
    override fun getRefreshKey(state: PagingState<Int, StarEntity>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StarEntity> {

        val page = params.key ?: 0
        return try {
            val entities = starDao.allStar(
                params.loadSize,
                page * params.loadSize
            )


            LoadResult.Page(
                data = entities,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

}