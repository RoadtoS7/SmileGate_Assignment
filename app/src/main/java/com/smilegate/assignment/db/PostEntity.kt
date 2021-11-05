package com.smilegate.assignment.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smilegate.assignment.domain.Post
import java.util.*

@Entity(tableName = "post")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val date: Date = Date()
) {
    fun toPost(): Post = Post(id, title, content, date)
}
