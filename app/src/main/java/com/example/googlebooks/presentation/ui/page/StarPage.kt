package com.example.googlebooks.presentation.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlebooks.R
import com.example.googlebooks.databinding.PageStarBinding
import com.example.googlebooks.presentation.ui.adapter.ContentsLoadStateAdapter
import com.example.googlebooks.presentation.ui.adapter.PagingAdapter
import com.example.googlebooks.presentation.ui.adapter.PagingLocalAdapter
import com.example.googlebooks.presentation.viewModel.StarPageViewModel
import com.example.googlebooks.presentation.viewModel.implementation.StarPageViewModelImplementation
import com.example.googlebooks.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class StarPage : Fragment(R.layout.page_star) {

    private val binding: PageStarBinding by viewBinding(PageStarBinding::bind)

    private val viewModel: StarPageViewModel by viewModels<StarPageViewModelImplementation>()

    @Inject
    lateinit var pagingLocalAdapter: PagingLocalAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenStarted {
            viewModel.pagerAdapter.collect {
                pagingAdapter()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.booksPagingData.collectLatest {
                pagingLocalAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.animeShow.collect {
                binding.noDataLottie.visible(it)
            }
        }

    }

    private fun pagingAdapter() {
        binding.rvLocal.adapter = pagingLocalAdapter
        binding.rvLocal.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(
            binding.rvLocal.context,
            DividerItemDecoration.VERTICAL
        )
        binding.rvLocal.addItemDecoration(dividerItemDecoration)
        pagingLocalAdapter.withLoadStateHeaderAndFooter(
            ContentsLoadStateAdapter { pagingLocalAdapter.retry() },
            ContentsLoadStateAdapter { pagingLocalAdapter.retry() }
        )

        pagingLocalAdapter.setStarDelete {
            viewModel.deleteItem(it)
        }
        viewModel.showAnime()

        pagingLocalAdapter.setLinkInfo {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(browserIntent)
        }

    }

}