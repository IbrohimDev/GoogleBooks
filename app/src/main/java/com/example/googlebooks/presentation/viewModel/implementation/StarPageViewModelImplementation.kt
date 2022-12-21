package com.example.googlebooks.presentation.viewModel.implementation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.remote.response.Item
import com.example.googlebooks.domain.repository.AppRepository
import com.example.googlebooks.presentation.viewModel.StarPageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class StarPageViewModelImplementation @Inject constructor(
    private val appRepository: AppRepository
) : StarPageViewModel, ViewModel() {
    override val pagerAdapter =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val booksPagingData = MutableSharedFlow<PagingData<StarEntity>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val animeShow =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    init {
        pagerAdapter.tryEmit(Unit)
    }

    override fun requestFromLocalDb() {
        appRepository.itemsFromLocalDb(viewModelScope).onEach {
            Timber.tag("HHH").d("Request search")
            booksPagingData.emit(it)
        }.launchIn(viewModelScope)
    }

    override fun showAnime() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.starCount().onEach {
                animeShow.emit(it.isEmpty())
                requestFromLocalDb()
            }.collect()

        }
    }

    override fun deleteItem(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteStarFromDb(bookId)
        }
    }

}