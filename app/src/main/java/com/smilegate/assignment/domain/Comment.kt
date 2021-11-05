package com.smilegate.assignment.domain

import android.os.Parcelable
import com.smilegate.assignment.db.CommentEntity
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Comment(
    val id: Int = 0,
    val content: String,
    val postId: Int,
    val writer: String,
    val encryptedPassword: String,
    val date: Date
) : Parcelable {
    fun toEntity(): CommentEntity = CommentEntity(id,  content, postId, writer, encryptedPassword, date)
}
