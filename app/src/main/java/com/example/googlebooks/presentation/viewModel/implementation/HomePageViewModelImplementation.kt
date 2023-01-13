package com.example.googlebooks.presentation.viewModel.implementation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.googlebooks.R
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.remote.response.Item
import com.example.googlebooks.domain.repository.AppRepository
import com.example.googlebooks.presentation.viewModel.HomePageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomePageViewModelImplementation @Inject constructor(
    private val appRepository: AppRepository,
    private val context: Context,
) : HomePageViewModel, ViewModel() {

    override val booksPagingData = MutableSharedFlow<PagingData<Item>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val pagerAdapter =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val animeShow =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val rvOpen  =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    private val _searchQuery = MutableStateFlow("")

    init {
        pagerAdapter.tryEmit(Unit)
        stateSearch(false)
        searchEditText(context.resources.getString(R.string.default_text_list))
    }

    override fun searchEditText(text: String) {
        _searchQuery.value = text
        requestSearch(text)
    }

    override fun requestSearch(text: String) {
        appRepository.searchedItems(text, viewModelScope).onEach {
            Timber.tag("HHH").d("Request search")
            booksPagingData.emit(it)
            rvOpen.tryEmit(Unit)
        }.launchIn(viewModelScope)
    }

    override fun stateSearch(state: Boolean) {
        animeShow.tryEmit(state)
    }

    override fun insertStarToDb(book: StarEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.insertStarToDb(book)
        }
    }

    override fun deleteStarFromDb(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteStarFromDb(bookId)
        }
    }

    override fun reSearch() {
        appRepository.searchedItems(_searchQuery.value, viewModelScope).onEach {
            Timber.tag("HHH").d("Request search")
            booksPagingData.emit(it)
            rvOpen.tryEmit(Unit)
        }.launchIn(viewModelScope)
    }


}