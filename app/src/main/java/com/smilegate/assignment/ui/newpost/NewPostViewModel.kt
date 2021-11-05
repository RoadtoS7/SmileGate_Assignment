package com.smilegate.assignment.ui.newpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.domain.BlogRepository
import com.smilegate.assignment.domain.SingleLiveEvent
import com.smilegate.assignment.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repository: BlogRepository
) : BaseViewModel() {

    private val _post: MutableLiveData<Post> = MutableLiveData()
    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()

    private val _showTitleEmptyMessage = SingleLiveEvent<Unit>()
    val showTitleEmptyMessage: LiveData<Unit>
    get() = _showTitleEmptyMessage

    private val _showContentEmptyMessage = SingleLiveEvent<Unit>()
    val showContentEmptyMessage: LiveData<Unit>
        get() = _showContentEmptyMessage

    private val _navigateBack = SingleLiveEvent<Unit>()
    val navigateBack: LiveData<Unit>
        get() = _navigateBack

    fun setPost(post: Post) {
        _post.value = post
        title.value = post.title
        content.value = post.content
    }

    fun writeNewPost() {
        viewModelScope.launch(vmExceptionHandler) {
            val post = _post.value
            val title = title.value
            val content = content.value

            if (title.isNullOrBlank()) {
                _showTitleEmptyMessage.call()
                return@launch
            }
            if (content.isNullOrBlank()) {
                _showContentEmptyMessage.call()
                return@launch
            }

            val newPost = if (post == null) {
                Post(title = title, content = content, date = Date())
            } else {
                Post(id = post.id, title = title, content = content, date = post.date)
            }
            repository.writePost(newPost)
            _navigateBack.call()
        }
    }
}
