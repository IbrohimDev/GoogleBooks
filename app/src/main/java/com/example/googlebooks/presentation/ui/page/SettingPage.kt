package com.example.googlebooks.presentation.ui.page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlebooks.R
import com.example.googlebooks.databinding.PageSettingBinding
import com.example.googlebooks.presentation.viewModel.SettingPageViewModel
import com.example.googlebooks.presentation.viewModel.implementation.SettingPageViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class SettingPage : Fragment(R.layout.page_setting) {

    private val binding: PageSettingBinding by viewBinding(PageSettingBinding::bind)
    private val viewModel: SettingPageViewModel by viewModels<SettingPageViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenStarted {
            viewModel.firstNameObserver.collect {
                observerFirstName(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.lastNameObserver.collect {
                observerLastName(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.ageTextObserver.collect {
                observerAgeText(it)
            }
        }

        binding.firstName.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString().trim()
                viewModel.setFirstName(text)
            }

        })
        binding.lastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString().trim()
                viewModel.setLastName(text)
            }

        })
        binding.ageText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString().trim()
                viewModel.setAgeText(text)
            }

        })
    }

    private fun observerFirstName(text: String) {
        binding.firstName.setText(text)
    }

    private fun observerLastName(text: String) {
        binding.lastName.setText(text)
    }

    private fun observerAgeText(text: String) {
        binding.ageText.setText(text)
    }
}