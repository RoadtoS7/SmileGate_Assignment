package com.smilegate.assignment.ui.postdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smilegate.assignment.*
import com.smilegate.assignment.databinding.ActivityPostDetailBinding
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.ui.base.BaseActivity
import com.smilegate.assignment.ui.comment.CommentActivity
import com.smilegate.assignment.ui.newpost.NewPostActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(R.layout.activity_post_detail) {
    override val viewModel: PostDetailViewModel by viewModels()

    private val post: Post?
        get() = intent.getParcelableExtra(KEY_POST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)

        post ?: finish()

        initToolbar(binding.toolbar)
        initWriteCommentButton(binding.writeComment)
    }

    private fun initWriteCommentButton(writeComment: TextView) {
        writeComment.setOnClickListener {
            val post = viewModel.post.value ?: return@setOnClickListener
            CommentActivity.navigate(this, post)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPost(post)
    }

    private fun initToolbar(toolbar: MaterialToolbar) {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    showDeleteDiaryDialog(this)
                }
                R.id.edit -> {
                    navigateToNewPost(post)
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun navigateToNewPost(post: Post?) {
        post?.let{
            NewPostActivity.navigate(this, it)
        } ?: finish()
    }

    private fun showDeleteDiaryDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.post_detail_delete_dialog_title))
            .setMessage(resources.getString(R.string.post_detail_delete_dialog_description))
            .setPositiveButton(resources.getString(R.string.post_detail_delete_dialog_accept)) { _, _ ->
                viewModel.deleteDiary()
                finish()
            }
            .setNegativeButton(resources.getString(R.string.diary_detail_delete_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        const val KEY_POST = "post"

        fun navigate(context: Context, post: Post) {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra(KEY_POST, post)
            context.startActivity(intent)
        }
    }
}