package com.example.googlebooks.data.remote.dataSource

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.googlebooks.BuildConfig
import com.example.googlebooks.data.local.dao.StarDao
import com.example.googlebooks.data.remote.api.BooksApi
import com.example.googlebooks.data.remote.response.Item
import com.example.googlebooks.util.isOnline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BooksDataSource @Inject constructor(
    private val context: Context,
    private val api: BooksApi,
    private val queryText: String,
    private val starDao: StarDao
) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        Timber.tag("HHH").d("Refresh key")
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        Timber.tag("HHH").d("We start")

        if (isOnline(context)) {

            val response = api.getBooks(
                queryText, pageSize, page,
                BuildConfig.API_KEY
            )



            Timber.tag("HHH").d(response.toString())

            return if (response.isSuccessful) {
                if (response.body()?.totalItems == 0) {
                    LoadResult.Page(listOf(), null, null)
                } else {


                    val article = response.body()
                    Timber.tag("HHH").d("posts $article")
                    val listCustom = article!!.items ?: listOf<Item>()

                    listCustom.forEach { item ->
                        val job = CoroutineScope(Dispatchers.IO).launch {
                            starDao.isStar(item.id).onEach {
                                if (it.isNotEmpty()) item.statusStar = true
                            }.collect()
                        }
                        job.onJoin
                    }


                    val nextKey = if (listCustom.size < pageSize) null else page + 1
                    val prevKey = if (page == 1) null else page - 1
                    LoadResult.Page(listCustom, prevKey, nextKey)
                }


            } else {
                LoadResult.Error(Throwable("Error with Connection"))
            }
        } else {
            return LoadResult.Page(listOf(), null, null)
        }
    }
}