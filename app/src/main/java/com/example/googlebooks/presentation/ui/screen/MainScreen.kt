package com.example.googlebooks.presentation.ui.screen

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlebooks.R
import com.example.googlebooks.databinding.ScreenMainBinding
import com.example.googlebooks.presentation.ui.adapter.PagerAdapter
import com.example.googlebooks.presentation.viewModel.MainScreenViewModel
import com.example.googlebooks.presentation.viewModel.implementation.MainScreenViewModelImplementation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {


    private val viewModel: MainScreenViewModel by viewModels<MainScreenViewModelImplementation>()
    private val binding: ScreenMainBinding by viewBinding(ScreenMainBinding::bind)
    private var _adapter: PagerAdapter? = null
    private val adapter: PagerAdapter get() = _adapter!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.bottomView.setOnItemSelectedListener {

            viewModel.selectBottomNavigationView(it)

            return@setOnItemSelectedListener true
        }

        lifecycleScope.launchWhenStarted {
            viewModel.pagerAdapter.collect {
                pagerAdapter()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bottomView.collect {
                selectionBottomView(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.countStar.collect {
                val badge = binding.bottomView.getOrCreateBadge(R.id.star_id)
                if (it > 0) {
                    badge.isVisible = true
                    badge.number = it
                } else {
                    badge.isVisible = false
                }
            }
        }
    }

    private fun selectionBottomView(item: MenuItem) {
        when (item.itemId) {
            R.id.home_id -> {
                binding.pager.setCurrentItem(0, true)
            }
            R.id.star_id -> {
                binding.pager.setCurrentItem(1, true)
            }
        }
    }

    private fun pagerAdapter() {
        _adapter = PagerAdapter(childFragmentManager, lifecycle)
        binding.pager.adapter = adapter
        binding.pager.isUserInputEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }
}