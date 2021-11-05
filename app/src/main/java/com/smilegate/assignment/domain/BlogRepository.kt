package com.smilegate.assignment.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.smilegate.assignment.db.BlogDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val dao: BlogDao
) {

    fun getPosts(): LiveData<List<Post>> = Transformations.map(dao.getAllPosts()) {
            it.map { postEntity -> postEntity.toPost() }

        }

    suspend fun deletePost(post: Post) = withContext(Dispatchers.IO) {
        dao.deletePost(post.toEntity())
    }

    suspend fun writePost(post: Post) {
        withContext(Dispatchers.IO) {
            dao.insertPost(post.toEntity())
        }
    }

    fun getCommentsByPost(postId: Int): LiveData<List<Comment>> {
        return Transformations.map(dao.getComments(postId)) { commentEntities ->
            commentEntities.map { it.toComment() }
        }
    }

    suspend fun writeComment(comment: Comment) = withContext(Dispatchers.IO){
        dao.insertComment(comment.toEntity())
    }

    suspend fun deleteComment(comment: Comment) = withContext(Dispatchers.IO){
        dao.deleteComment(comment.toEntity())
    }

    suspend fun getPost(postId: Int): Post = withContext(Dispatchers.IO){
        return@withContext dao.getPost(postId).toPost()
    }
}
