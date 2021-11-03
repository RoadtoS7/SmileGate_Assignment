package com.smilegate.assignment.db

import androidx.room.*

@Dao
interface BlogDao {
    @Query("SELECT * FROM post")
    fun getAllPosts(): List<PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(postEntity: PostEntity)

    @Delete()
    fun deletePost(postEntity: PostEntity)

    @Update
    fun updatePost(postEntity: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryEntity: CategoryEntity)

    @Transaction
    @Query("SELECT * FROM post where :categoryId == categoryId")
    fun getPostsByCategory(categoryId: Int): List<PostEntity>

    @Query("SELECT * FROM category")
    fun getAllCategory(): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE parentCategoryId = :parentCategoryId")
    fun getChildrenCategories(parentCategoryId: Int): List<CategoryEntity>

    @Transaction
    @Query("SELECT * FROM comment WHERE :postId == postId")
    fun getComments(postId: Int): List<CommentEntity>

    @Insert
    fun insertComment(commentEntity: CommentEntity)

    @Delete
    fun deleteComment(commentEntity: CommentEntity)
}
