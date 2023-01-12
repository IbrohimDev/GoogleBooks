package com.example.googlebooks.presentation.viewModel

import kotlinx.coroutines.flow.Flow

interface SettingPageViewModel {
    val firstNameObserver:Flow<String>
    val lastNameObserver:Flow<String>
    val ageTextObserver:Flow<String>

    fun setFirstName(text: String)

    fun setLastName(text: String)

    fun setAgeText(text: String)
}