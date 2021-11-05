package com.smilegate.assignment.domain

import android.os.Parcelable
import com.smilegate.assignment.db.PostEntity
import kotlinx.parcelize.Parcelize

import java.util.Date

@Parcelize
class Post(
    val id: Int = 0,
    val title: String,
    val content: String,
    val date: Date
):Parcelable {
    fun toEntity(): PostEntity = PostEntity(id = id, title = title, content = content, date = date)
}
