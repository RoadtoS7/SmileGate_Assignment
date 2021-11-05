package com.smilegate.assignment.ui.comment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smilegate.assignment.R
import com.smilegate.assignment.domain.Comment
import com.smilegate.assignment.databinding.DialogDeleteAuthCheckBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentDeleteAuthCheckDialog : DialogFragment() {
    private val viewModel: CommentDeleteAuthCheckViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_FullScreenDialog)
        val comment = arguments?.get(KEY_COMMENT) as Comment
        viewModel.setComment(comment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogDeleteAuthCheckBinding.inflate(inflater, container, false)

        binding.checkPassword.setOnClickListener {
            val password = binding.password.text.toString()
            val result = viewModel.checkPassword(password)
            if (result) showConfirmDialog(requireContext())
        }
        binding.cancel.setOnClickListener { this.dismiss() }

        viewModel.completeDelete.observe(this) {
            dismiss()
        }
        return binding.root
    }

    private fun showConfirmDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.delete_auth_check_dialog_title))
            .setMessage(resources.getString(R.string.delete_auth_check_dialog_description))
            .setPositiveButton(resources.getString(R.string.delete_auth_check_dialog_accept)) { dialog, _ ->
                viewModel.deleteComment()
            }
            .setNegativeButton(resources.getString(R.string.delete_auth_check_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        private const val KEY_COMMENT = "comment"

        fun getCommentBundle(comment: Comment): Bundle {
            val bundle = Bundle()
            bundle.putParcelable(KEY_COMMENT, comment)
            return bundle
        }
    }
}
