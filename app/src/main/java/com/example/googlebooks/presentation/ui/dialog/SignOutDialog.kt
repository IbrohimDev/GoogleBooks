package com.example.googlebooks.presentation.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlebooks.R
import com.example.googlebooks.databinding.DialogSignOutBinding

class SignOutDialog : DialogFragment(R.layout.dialog_sign_out) {

    private val binding: DialogSignOutBinding by viewBinding(DialogSignOutBinding::bind)
    private var yesListener: (() -> (Unit))? = null

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.yesBtn.setOnClickListener {
            yesListener?.invoke()
            dismiss()
        }

        binding.noBtn.setOnClickListener {
            dismiss()
        }
    }

    fun setYesListener(block: () -> (Unit)) {
        yesListener = block
    }
}