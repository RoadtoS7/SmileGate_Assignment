package com.smilegate.assignment.ui.newpost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smilegate.assignment.BR
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.R
import com.smilegate.assignment.databinding.ActivityNewPostBinding
import com.smilegate.assignment.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPostActivity : BaseActivity<ActivityNewPostBinding>(R.layout.activity_new_post) {
    override val viewModel: NewPostViewModel by viewModels()

    private val post: Post?
        get() = intent.getParcelableExtra(KEY_POST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.setVariable(BR.viewModel, viewModel)
        initWritePostButton(binding.writeNewPost)
        initCancelButton(binding.cancel)

        post?.let { viewModel.setPost(it) }

        viewModel.showContentEmptyMessage.observe(this) {
            showContentEmptyMessage(this)
        }

        viewModel.showTitleEmptyMessage.observe(this) {
            showTitleEmptyMessage(this)
        }

        viewModel.navigateBack.observe(this) {
            finish()
        }
    }

    private fun initCancelButton(cancel: ImageButton) {
        cancel.setOnClickListener { finish() }
    }

    private fun showTitleEmptyMessage(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.new_post_impossible_dialog_title))
            .setMessage(resources.getString(R.string.new_post_title_impossible_dialog_description))
            .setPositiveButton(resources.getString(R.string.new_post_impossible_dialog_accept)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showContentEmptyMessage(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.new_post_impossible_dialog_title))
            .setMessage(resources.getString(R.string.new_post_content_impossible_dialog_description))
            .setPositiveButton(resources.getString(R.string.new_post_impossible_dialog_accept)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun initWritePostButton(writeNewPost: Button) {
        writeNewPost.setOnClickListener {
            viewModel.writeNewPost()
        }
    }

    companion object {
        private const val KEY_POST = "post"

        fun navigate(context: Context, post: Post? = null) {
            val intent = Intent(context, NewPostActivity::class.java)
            intent.putExtra(KEY_POST, post)
            context.startActivity(intent)
        }
    }
}