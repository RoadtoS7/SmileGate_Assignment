package com.smilegate.assignment.ui.postdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.domain.BlogRepository
import com.smilegate.assignment.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repository: BlogRepository
) : BaseViewModel() {
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun deleteDiary() {
        viewModelScope.launch(vmExceptionHandler) {
            val post = post.value ?: return@launch
            repository.deletePost(post)
        }
    }

    fun loadPost(post: Post?) {
        viewModelScope.launch(vmExceptionHandler) {
            post ?: return@launch
            _post.value = repository.getPost(post.id)
        }
    }
}
