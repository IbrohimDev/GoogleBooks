package com.example.googlebooks.presentation.viewModel.implementation


import com.example.googlebooks.domain.model.ManageUiSealed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlebooks.domain.repository.AppRepository
import com.example.googlebooks.presentation.viewModel.RegisterScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class RegisterScreenViewModelImplementation @Inject constructor(
    private val repository: AppRepository
) : RegisterScreenViewModel, ViewModel() {

    override val setToggleChange =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val setPasswordChange =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val setEmailChange =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val invalidInput =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val loadingPage =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val successAuth =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val failureAuth =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    override var toggleEnter: Boolean = false
    override var isPassword: Boolean = false
    override var isEmail: Boolean = false
    override var emailText = ""
    override var passwordText = ""

    init {
        repository.checkUserValid().onEach { isUser ->
            if (isUser) {
                successAuth.emit(Unit)
            }
        }.launchIn(viewModelScope)
    }

    override fun changeToggleText() {
        toggleEnter = !toggleEnter
        setToggleChange.tryEmit(toggleEnter)
    }

    override fun passwordTextChange(text: String) {
        passwordText = text
        isPassword = text.length >= 6
        setPasswordChange.tryEmit(isPassword)
    }

    override fun emailTextChange(text: String) {
        emailText = text
        isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
        setEmailChange.tryEmit(isEmail)
    }

    override fun enterButtonClick() {
        if (isPassword && isEmail) {
            if (toggleEnter) {
                signInUser()
            } else {
                createUser()
            }
        } else {
            invalidInput.tryEmit(Unit)
        }
    }

    override fun createUser() {
        repository.createUser(emailText, passwordText).onEach { status ->
            when (status) {
                ManageUiSealed.Loading -> {
                    loadingPage.emit(true)
                }
                ManageUiSealed.Success -> {
                    loadingPage.emit(false)
                    successAuth.emit(Unit)
                }
                ManageUiSealed.Failure -> {
                    loadingPage.emit(false)
                    failureAuth.emit(Unit)
                }
            }

        }.launchIn(viewModelScope)
    }

    override fun signInUser() {
        repository.signInUser(emailText, passwordText).onEach { status ->
            when (status) {
                ManageUiSealed.Loading -> {
                    loadingPage.emit(true)
                }
                ManageUiSealed.Success -> {
                    loadingPage.emit(false)
                    successAuth.emit(Unit)
                }
                ManageUiSealed.Failure -> {
                    loadingPage.emit(false)
                    failureAuth.emit(Unit)
                }
            }
        }.launchIn(viewModelScope)
    }


}