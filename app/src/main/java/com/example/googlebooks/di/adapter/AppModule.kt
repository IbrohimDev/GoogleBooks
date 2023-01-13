package com.example.googlebooks.di.adapter


import android.content.Context
import com.example.googlebooks.data.local.dao.StarDao
import com.example.googlebooks.presentation.ui.adapter.PagingAdapter
import com.example.googlebooks.presentation.ui.adapter.PagingLocalAdapter
import com.example.googlebooks.presentation.ui.dialog.SignOutDialog
import dagger.Module
import dagger.Provides

import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent


@Module
@InstallIn(FragmentComponent::class)
class AppModule {

    @Provides
    fun pagingAdapter(context: Context, starDao: StarDao) = PagingAdapter(context, starDao)

    @Provides
    fun pagingLocalAdapter(context: Context) = PagingLocalAdapter(context)

    @Provides
    fun singOutDialog() = SignOutDialog()

}