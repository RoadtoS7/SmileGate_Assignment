package com.smilegate.assignment.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smilegate.assignment.domain.Comment
import java.util.*

@Entity(tableName = "comment")
class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val postId: Int,
    val writer: String,
    val writerPw: String,
    val date: Date
) {
    fun toComment(): Comment = Comment(id, content, postId, writer, writerPw, date)
}
