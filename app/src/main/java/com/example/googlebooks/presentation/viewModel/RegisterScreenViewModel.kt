package com.example.googlebooks.presentation.viewModel

import kotlinx.coroutines.flow.Flow

interface RegisterScreenViewModel {
    val setToggleChange: Flow<Boolean>
    val setPasswordChange: Flow<Boolean>
    val setEmailChange: Flow<Boolean>
    val invalidInput: Flow<Unit>
    val loadingPage: Flow<Boolean>
    val successAuth: Flow<Unit>
    val failureAuth: Flow<Unit>

    var toggleEnter: Boolean
    var isPassword: Boolean
    var isEmail: Boolean
    var emailText: String
    var passwordText: String


    fun changeToggleText()

    fun passwordTextChange(text: String)

    fun emailTextChange(text: String)

    fun enterButtonClick()

    fun createUser()

    fun signInUser()
}