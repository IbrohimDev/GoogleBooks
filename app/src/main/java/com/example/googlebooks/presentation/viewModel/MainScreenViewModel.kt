package com.example.googlebooks.presentation.viewModel

import android.view.MenuItem
import kotlinx.coroutines.flow.Flow


interface MainScreenViewModel {
    val pagerAdapter: Flow<Unit>
    val bottomView: Flow<MenuItem>
    val countStar: Flow<Int>

    fun selectBottomNavigationView(item: MenuItem)

    fun updateBadge()
}