package com.smilegate.assignment.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "comment",
    foreignKeys = [
        ForeignKey(entity = PostEntity::class, parentColumns = ["id"], childColumns = ["postId"])
    ]
)
class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Date,
    val content: String,
    val postId: Int,
    val parentCommentId: Int? = null,
    val writer: String,
    val writerPw: String
)
