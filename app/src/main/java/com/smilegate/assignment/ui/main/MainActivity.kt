package com.smilegate.assignment.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.smilegate.assignment.ui.base.BaseActivity
import com.smilegate.assignment.R
import com.smilegate.assignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.smilegate.assignment.BR
import com.smilegate.assignment.ui.newpost.NewPostActivity
import com.smilegate.assignment.ui.postdetail.PostDetailActivity
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override val viewModel: MainViewModel by viewModels()

    private val postAdapter by lazy {
        PostAdapter(PostAdapter.OnClickListener{
            viewModel.navigateToPostDetail(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)

        initToolbar(binding.topToolBar)
        initPostList(binding.posts)
        observeUi(viewModel)
    }

    private fun initToolbar(topToolBar: MaterialToolbar) {
        topToolBar.setOnMenuItemClickListener { menuItem ->
            if(menuItem.itemId == R.id.write_new_post) {
                NewPostActivity.navigate(this)
            }
            true
        }
    }

    private fun initPostList(posts: RecyclerView) {
        posts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        posts.adapter = postAdapter
    }

    private fun observeUi(viewModel: MainViewModel) {
        viewModel.posts.observe(this) {
            postAdapter.submitList(it)
            Timber.d("size: ${it.size}")
        }

        viewModel.navigateToPostDetail.observe(this) {
            PostDetailActivity.navigate(this, it)
        }
    }
}
