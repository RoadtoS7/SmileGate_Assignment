package com.smilegate.assignment.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    /** viewModelScope 에서 Exception 이 발생할 시 처리하는 핸들러 */
    val vmExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber.i("$coroutineContext $throwable")
    }
}
