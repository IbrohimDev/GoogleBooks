package com.example.googlebooks.presentation.viewModel.implementation

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlebooks.domain.repository.AppRepository
import com.example.googlebooks.presentation.viewModel.MainScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModelImplementation @Inject constructor(
    private val appRepository: AppRepository
) : MainScreenViewModel, ViewModel() {
    override val pagerAdapter =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val bottomView =
        MutableSharedFlow<MenuItem>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val countStar =
        MutableSharedFlow<Int>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        updateBadge()
    }
    override fun selectBottomNavigationView(item: MenuItem) {
        bottomView.tryEmit(item)
    }

    override fun updateBadge() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.starCount().onEach {
              countStar.emit(it.size)
            }.collect()
        }

    }


    init {
        pagerAdapter.tryEmit(Unit)
    }


}