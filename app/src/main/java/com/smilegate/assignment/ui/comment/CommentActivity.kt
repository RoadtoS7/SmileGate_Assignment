package com.smilegate.assignment.ui.comment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.smilegate.assignment.ui.base.BaseActivity
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.R
import com.smilegate.assignment.databinding.ActivityCommentBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar


@AndroidEntryPoint
class CommentActivity : BaseActivity<ActivityCommentBinding>(R.layout.activity_comment) {
    override val viewModel: CommentViewModel by viewModels()

    private val post: Post?
        get() = intent.getParcelableExtra(KEY_POST)

    private val commendAdapter = CommentAdapter(
        CommentAdapter.OnDeleteClickListener {
            val dialog = CommentDeleteAuthCheckDialog()
            val bundle = CommentDeleteAuthCheckDialog.getCommentBundle(it)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, COMMENT_DELETE_DIALOG_TAG)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        post?.let { viewModel.setPost(it) } ?: finish()

        observeUi()

        initToolbar(binding.toolbar)
        initComments(binding.comments)
        initWriteCommentWindow(binding.writeCommentButton)
    }

    private fun initComments(comments: RecyclerView) {
        comments.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        comments.adapter = commendAdapter
    }

    private fun initWriteCommentWindow(writeCommentButton: Button) {
        writeCommentButton.setOnClickListener {
            val nickname = binding.nickname.text.toString()
            val password = binding.password.text.toString()
            val content = binding.commentContent.text.toString()
            viewModel.writeComment(nickname, password, content)
        }

        val onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus) scrollToBottom(binding.writeCommentButton)
        }
        binding.nickname.onFocusChangeListener = onFocusChangeListener
        binding.password.onFocusChangeListener = onFocusChangeListener
        binding.commentContent.onFocusChangeListener = onFocusChangeListener
    }

    private fun scrollToBottom(postContents: TextView) {
        binding.scrollView.postDelayed({
            binding.scrollView.smoothScrollBy(
                0,
                (postContents.height + postContents.y).toInt()
            )
        }, 200)
    }


    private fun observeUi() {
        viewModel.comments.observe(this) {
            commendAdapter.submitList(it)
        }

        viewModel.completeWriteComment.observe(this) {
            binding.nickname.setText("")
            binding.password.setText("")
            binding.commentContent.setText("")
            binding.comments.requestFocus()
        }
    }

    private fun initToolbar(toolbar: MaterialToolbar) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    companion object {
        private const val KEY_POST = "post"
        private const val COMMENT_DELETE_DIALOG_TAG = "COMMENT_DELETE_DIALOG_TAG"

        fun navigate(context: Context, post: Post) {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(KEY_POST, post)
            context.startActivity(intent)
        }
    }
}