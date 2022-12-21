package com.example.googlebooks.di.repository

import com.example.googlebooks.domain.repository.AppRepository
import com.example.googlebooks.domain.repository.implementation.AppRepositoryImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun getAppRepository(impl: AppRepositoryImplementation): AppRepository
}