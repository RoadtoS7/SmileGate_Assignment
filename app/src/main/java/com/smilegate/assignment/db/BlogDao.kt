package com.smilegate.assignment.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BlogDao {
    @Query("SELECT * FROM post")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Query("SELECT * FROM post WHERE id = :postId LIMIT 1")
    suspend fun getPost(postId: Int): PostEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postEntity: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPreparedPost(postEntity: PostEntity)

    @Delete()
    suspend fun deletePost(postEntity: PostEntity)

    @Update
    suspend fun updatePost(postEntity: PostEntity)

    @Transaction
    @Query("SELECT * FROM comment WHERE :postId == postId")
    fun getComments(postId: Int): LiveData<List<CommentEntity>>

    @Insert
    suspend fun insertComment(commentEntity: CommentEntity)

    @Insert
    fun insertPreparedComments(comments: List<CommentEntity>)

    @Delete
    suspend fun deleteComment(commentEntity: CommentEntity)
}
