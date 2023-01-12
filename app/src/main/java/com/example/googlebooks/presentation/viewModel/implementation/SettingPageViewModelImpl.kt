package com.example.googlebooks.presentation.viewModel.implementation

import androidx.lifecycle.ViewModel
import com.example.googlebooks.domain.repository.AppRepository
import com.example.googlebooks.presentation.viewModel.SettingPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingPageViewModelImpl @Inject constructor(
    private val repository: AppRepository
) : SettingPageViewModel, ViewModel() {

    override val firstNameObserver =
        MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val lastNameObserver =
        MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val ageTextObserver =
        MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        firstNameObserver.tryEmit(repository.getFirstName())
        lastNameObserver.tryEmit(repository.getLastName())
        ageTextObserver.tryEmit(repository.getAge())
    }


    override fun setFirstName(text: String) {
        repository.setFirstName(text)
    }

    override fun setLastName(text: String) {
        repository.setLastName(text)
    }

    override fun setAgeText(text: String) {
        repository.setAge(text)
    }

}