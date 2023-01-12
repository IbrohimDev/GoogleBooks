package com.example.googlebooks.domain.model

sealed class ManageUiSealed {
    object Loading : ManageUiSealed()
    object Success : ManageUiSealed()
    object Failure : ManageUiSealed()
}
