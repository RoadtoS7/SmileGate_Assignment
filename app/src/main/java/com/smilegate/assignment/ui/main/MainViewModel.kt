package com.smilegate.assignment.ui.main

import androidx.lifecycle.LiveData
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.domain.BlogRepository
import com.smilegate.assignment.domain.SingleLiveEvent
import com.smilegate.assignment.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: BlogRepository
) : BaseViewModel() {
    private val _navigateToPostDetail = SingleLiveEvent<Post>()
    val navigateToPostDetail: LiveData<Post>
        get() = _navigateToPostDetail

    fun navigateToPostDetail(post: Post) {
        _navigateToPostDetail.value = post
    }

    val posts: LiveData<List<Post>> = repository.getPosts()
}
