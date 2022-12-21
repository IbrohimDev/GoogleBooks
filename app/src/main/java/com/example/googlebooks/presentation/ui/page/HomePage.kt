package com.example.googlebooks.presentation.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlebooks.R
import com.example.googlebooks.databinding.PageHomeBinding
import com.example.googlebooks.presentation.ui.adapter.ContentsLoadStateAdapter
import com.example.googlebooks.presentation.ui.adapter.PagingAdapter
import com.example.googlebooks.presentation.viewModel.HomePageViewModel
import com.example.googlebooks.presentation.viewModel.implementation.HomePageViewModelImplementation
import com.example.googlebooks.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {

    private val binding: PageHomeBinding by viewBinding(PageHomeBinding::bind)
    private val viewModel: HomePageViewModel by viewModels<HomePageViewModelImplementation>()

    @Inject
    lateinit var pagingAdapter: PagingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.tag("HH").d("onViewCreated")

        binding.searchQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Timber.tag("HHH").d("onTextChange $text")

            }

            override fun afterTextChanged(text: Editable?) {
                Timber.tag("HHH").d("after Text change %s", text.toString())

                text?.let {
                    val searchText = text.trim()
                    if (searchText.isNotEmpty()) {
                        viewModel.stateSearch(false)
                        viewModel.searchEditText(searchText.toString())
                    } else
                        viewModel.stateSearch(true)
                }
            }

        })

        lifecycleScope.launchWhenStarted {
            viewModel.pagerAdapter.collect {
                pagingAdapter()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.booksPagingData.collectLatest {
                pagingAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.animeShow.collect {
                showAnime(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.rvOpen.collect {
                binding.rvSearch.isClickable = true
            }
        }
    }


    override fun onResume() {
        super.onResume()
        binding.rvSearch.isClickable = false
        viewModel.reSearch()
    }

    private fun showAnime(state: Boolean) {
        binding.noDataLottie.visible(state)
        binding.rvSearch.visible(!state)
    }

    private fun pagingAdapter() {
        binding.rvSearch.adapter = pagingAdapter
        binding.rvSearch.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(
            binding.rvSearch.context,
            DividerItemDecoration.VERTICAL
        )
        binding.rvSearch.addItemDecoration(dividerItemDecoration)
        pagingAdapter.withLoadStateHeaderAndFooter(
            ContentsLoadStateAdapter { pagingAdapter.retry() },
            ContentsLoadStateAdapter { pagingAdapter.retry() }
        )

        pagingAdapter.setStarDelete {
            viewModel.deleteStarFromDb(it)
        }
        pagingAdapter.setStarInsert {
            viewModel.insertStarToDb(it)
        }
        pagingAdapter.setLinkInfo {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(browserIntent)
        }

    }
}