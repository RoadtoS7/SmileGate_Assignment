package com.smilegate.assignment.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smilegate.assignment.domain.BlogRepository
import com.smilegate.assignment.domain.Comment
import com.smilegate.assignment.domain.SingleLiveEvent
import com.smilegate.assignment.di.AuthManager
import com.smilegate.assignment.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentDeleteAuthCheckViewModel @Inject constructor(
     private val repository: BlogRepository
) : BaseViewModel() {
    private var comment: Comment? = null

    private val _needPasswordMessage = MutableLiveData(false)
    val needPasswordMessage: LiveData<Boolean>
        get() = _needPasswordMessage

    private val _wrongPasswordMessage = MutableLiveData(false)
    val wrongPasswordMessage: LiveData<Boolean>
        get() = _wrongPasswordMessage

    private val _completeDelete = SingleLiveEvent<Unit>()
    val completeDelete: LiveData<Unit>
        get() = _completeDelete


    fun setComment(comment: Comment) {
        this.comment = comment
    }

    fun checkPassword(password: String): Boolean {
        val isPasswordBlank = password.isBlank()
        _needPasswordMessage.value = isPasswordBlank
        if (isPasswordBlank) return false

        val isValid = comment?.let {
            AuthManager.isValidPassword(password, it.encryptedPassword)
        } ?: false

        _wrongPasswordMessage.value = !isValid
        return isValid
    }

    fun deleteComment() {
        viewModelScope.launch(vmExceptionHandler) {
            comment?.let { repository.deleteComment(it) }
            _completeDelete.call()
        }
    }
}