package com.example.googlebooks.presentation.viewModel

import kotlinx.coroutines.flow.Flow

interface SettingPageViewModel {
    val firstNameObserver: Flow<String>
    val lastNameObserver: Flow<String>
    val ageTextObserver: Flow<String>
    val signOutDialogObserver: Flow<Unit>
    val logOutAppObserver: Flow<Unit>


    fun setFirstName(text: String)

    fun setLastName(text: String)

    fun setAgeText(text: String)

    fun clickSignOut()

    fun signOutDialog()
}