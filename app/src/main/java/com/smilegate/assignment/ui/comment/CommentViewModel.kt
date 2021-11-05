package com.smilegate.assignment.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.domain.BlogRepository
import com.smilegate.assignment.domain.Comment
import com.smilegate.assignment.domain.SingleLiveEvent
import com.smilegate.assignment.util.AuthManager
import com.smilegate.assignment.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: BlogRepository
) : BaseViewModel() {

    private val post = MutableLiveData<Post>()
    val postTitle: LiveData<String> = Transformations.map(post) { it.title }

    val comments: LiveData<List<Comment>> = Transformations.switchMap(post) {
        repository.getCommentsByPost(it.id)
    }
    val isCommentEmpty: LiveData<Boolean> = Transformations.map(comments) { it.isNotEmpty() }


    private val _completeWriteComment = SingleLiveEvent<Unit>()
    val completeWriteComment: LiveData<Unit>
        get() = _completeWriteComment

    fun setPost(post: Post) {
        this.post.value = post
    }

    fun writeComment(nickname: String, password: String, content: String) {
        viewModelScope.launch(vmExceptionHandler) {
            if (nickname.isBlank()) return@launch
            if (password.isBlank()) return@launch
            if (content.isBlank()) return@launch

            val post = post.value ?: return@launch
            val comment = Comment(
                postId = post.id,
                writer = nickname,
                encryptedPassword = AuthManager.encryptPassword(password),
                content = content,
                date = Date()
            )

            repository.writeComment(comment)
            _completeWriteComment.call()
        }
    }
}
