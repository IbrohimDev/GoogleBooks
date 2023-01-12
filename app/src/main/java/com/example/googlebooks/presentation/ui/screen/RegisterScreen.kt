package com.example.googlebooks.presentation.ui.screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlebooks.R
import com.example.googlebooks.databinding.ScreenRegisterBinding
import com.example.googlebooks.presentation.viewModel.RegisterScreenViewModel
import com.example.googlebooks.presentation.viewModel.implementation.RegisterScreenViewModelImplementation

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {

    private val binding: ScreenRegisterBinding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel: RegisterScreenViewModel by viewModels<RegisterScreenViewModelImplementation>()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenStarted {
            viewModel.setToggleChange.collect {
                observeToggleChangeText(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.setPasswordChange.collect {
                observePasswordChangeText(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.setEmailChange.collect {
                observeEmailChangeText(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.invalidInput.collect {
                invalidInput()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.loadingPage.collect {
                loadingPage(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.successAuth.collect {
                navController.navigate(R.id.mainScreen)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.failureAuth.collect {
                invalidInput()
            }
        }

        binding.signUpOrSignInToggle.setOnClickListener {
            viewModel.changeToggleText()
        }
        binding.signupOrSignInButton.setOnClickListener {
            viewModel.enterButtonClick()
        }


        binding.passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString().trim()
                viewModel.passwordTextChange(text)
            }

        })


        binding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString().trim()
                viewModel.emailTextChange(text)

            }

        })


    }

    private fun loadingPage(status: Boolean) {
        when (status) {
            true -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.registerScreen.isEnabled = false
                binding.registerScreen.alpha = 0.5F
            }
            else -> {
                binding.progressBar.visibility = View.GONE
                binding.registerScreen.isEnabled = true
                binding.registerScreen.alpha = 1f
            }
        }
    }

    private fun invalidInput() {
        Toast.makeText(
            requireContext(), "Invalid Input",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun observeToggleChangeText(toggleStatus: Boolean) {
        if (toggleStatus) {
            binding.signUpOrSignInToggle.text = resources.getString(R.string.signup_text)
            binding.signupOrSignInButton.text = resources.getString(R.string.sign_in_text)
        } else {
            binding.signUpOrSignInToggle.text = resources.getString(R.string.sign_in_text)
            binding.signupOrSignInButton.text = resources.getString(R.string.signup_text)
        }
    }

    private fun observePasswordChangeText(passwordTextStatus: Boolean) {
        if (passwordTextStatus) {
            binding.passwordInputOuter.error = null
        } else {
            binding.passwordInputOuter.error = "Length should be min 6 character"
        }

        binding.passwordInputOuter.isErrorEnabled = !passwordTextStatus
    }

    private fun observeEmailChangeText(emailTextStatus: Boolean) {
        binding.emailInputOuter.isErrorEnabled = !emailTextStatus
        if (emailTextStatus) {
            binding.emailInput.error = null
        } else {
            binding.emailInputOuter.error = "Error message"

        }
    }
}