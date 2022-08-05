package com.pyrinnl.githubrepo.ui.dialog


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pyrinnl.githubrepo.R


class ErrorDialogFragment : DialogFragment() {

    private val errorMessage: String
    get() = requireArguments().getString(ARG_ERROR_MESSAGE)?: throw IllegalArgumentException("Error message must be not null")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(errorMessage)
            .setPositiveButton(getString(R.string.alert_dialog_button_text), null )
            .create()
    }


    companion object {
        private const val ARG_ERROR_MESSAGE = "error_message"

        fun show(manager: FragmentManager, errorMessage: String) {
            val dialogFragment = ErrorDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_ERROR_MESSAGE to errorMessage)
            dialogFragment.show(manager, null)
        }
    }
}