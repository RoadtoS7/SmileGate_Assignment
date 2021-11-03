package com.smilegate.assignment

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smilegate.assignment.db.*
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class BlogDatabaseTest {
    private lateinit var blogDao: BlogDao
    private lateinit var db: BlogDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BlogDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        blogDao = db.dao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetPost() {
        val testCategoryId = 1001
        val categoryEntity = CategoryEntity(
            id = testCategoryId,
            parentCategoryId = null,
            categoryName = "categoryName"
        )

        val postEntity = PostEntity(
            title = "postTitle",
            content = "postContent",
            image = "postImage",
            categoryId = testCategoryId,
            date = Date()
        )

        blogDao.insertCategory(categoryEntity)
        blogDao.insertPost(postEntity)

        val testPostEntity = blogDao.getPostsByCategory(testCategoryId)[0]
        assertEquals(postEntity.categoryId, testPostEntity.categoryId)
    }

    @Test
    @Throws(Exception::class)
    fun deletePost() {
        val testCategoryId = 1001
        val categoryEntity = CategoryEntity(
            id = testCategoryId,
            parentCategoryId = null,
            categoryName = "categoryName"
        )
        val testPostId = 1001
        val postEntity = PostEntity(
            id = testPostId,
            title = "postTitle",
            content = "postContent",
            image = "postImage",
            categoryId = testCategoryId,
            date = Date()
        )

        blogDao.insertCategory(categoryEntity)
        blogDao.insertPost(postEntity)
        blogDao.deletePost(postEntity)

        val postIds = blogDao.getAllPosts()
            .map { it.id }
            .toSet()
        assertFalse(postIds.contains(testPostId))
    }

    @Test
    @Throws(Exception::class)
    fun updatePost() {
        // given
        val testCategoryId = 1001
        val categoryEntity = CategoryEntity(
            id = testCategoryId,
            parentCategoryId = null,
            categoryName = "categoryName"
        )
        val testPostId = 1001
        val postEntity = PostEntity(
            id = testPostId,
            title = "postTitle",
            content = "postContent",
            image = "postImage",
            categoryId = testCategoryId,
            date = Date()
        )

        blogDao.insertCategory(categoryEntity)
        blogDao.insertPost(postEntity)

        val updatedContent = "updatedContent"
        val contentChangedPost = PostEntity(
            id = postEntity.id,
            title = postEntity.title,
            content = updatedContent,
            categoryId = postEntity.categoryId,
            image = postEntity.image,
            date = postEntity.date
        )

        // when
        blogDao.updatePost(contentChangedPost)
        val afterPostEntity = blogDao.getAllPosts()[0]

        // then
        assertThat(afterPostEntity.content, `is`(updatedContent))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCategory() {
        val categoryEntity = CategoryEntity(
            parentCategoryId = null,
            categoryName = "categoryName"
        )

        blogDao.insertCategory(categoryEntity)
        val firstCategoryEntity = blogDao.getAllCategory()[0]
        assertEquals(categoryEntity.categoryName, firstCategoryEntity.categoryName)
    }

    @Test
    @Throws(Exception::class)
    fun getPostsByCategory() {
        val testCategoryId = 1001
        val categoryEntity = CategoryEntity(
            id = testCategoryId,
            parentCategoryId = null,
            categoryName = "categoryName"
        )

        val firstPostId = 1
        val secondPostId = 2
        val firstPostEntity = PostEntity(
            id = firstPostId,
            title = "postTitle",
            content = "postContent",
            image = "postImage",
            categoryId = testCategoryId,
            date = Date()
        )
        val secondPostEntity = PostEntity(
            id = secondPostId,
            title = "postTitle",
            content = "postContent",
            image = "postImage",
            categoryId = testCategoryId,
            date = Date()
        )

        blogDao.insertCategory(categoryEntity)
        blogDao.insertPost(firstPostEntity)
        blogDao.insertPost(secondPostEntity)

        val resultPostSet = blogDao.getPostsByCategory(testCategoryId)
            .map { it.id }
            .toSet()

        assertTrue(resultPostSet.contains(firstPostId))
        assertTrue(resultPostSet.contains(secondPostId))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAllCategory() {
        val travelCategory = CategoryEntity(
            parentCategoryId = null,
            categoryName = "travel"
        )
        val foodCategory = CategoryEntity(
            parentCategoryId = null,
            categoryName = "food"
        )

        blogDao.insertCategory(travelCategory)
        blogDao.insertCategory(foodCategory)

        val categorySet = blogDao.getAllCategory()
            .map { it.categoryName }
            .toSet()

        assertTrue(categorySet.contains(travelCategory.categoryName))
        assertTrue(categorySet.contains(foodCategory.categoryName))
    }

    @Test
    @Throws(Exception::class)
    fun getChildrenCategories() {
        // given
        val parentCategoryId = 1001
        val parentCategoryEntity = CategoryEntity(
            id = parentCategoryId,
            parentCategoryId = null,
            categoryName = "categoryName"
        )

        val firstChildCategoryEntity = CategoryEntity(
            parentCategoryId = parentCategoryId,
            categoryName = "categoryName"
        )
        val secondChildCategoryEntity = CategoryEntity(
            parentCategoryId = parentCategoryId,
            categoryName = "categoryName"
        )

        blogDao.insertCategory(parentCategoryEntity)
        blogDao.insertCategory(firstChildCategoryEntity)
        blogDao.insertCategory(secondChildCategoryEntity)

        // when
        val childrenCategories = blogDao.getChildrenCategories(parentCategoryId)

        // assert
        assertThat(childrenCategories[0].parentCategoryId, `is`(parentCategoryId))
        assertThat(childrenCategories[1].parentCategoryId, `is`(parentCategoryId))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetComments() {
        // given
        val categoryId = 1
        val categoryEntity = CategoryEntity(
            id = categoryId,
            parentCategoryId = null,
            categoryName = "categoryName"
        )

        val postId = 1
        val postEntity = PostEntity(
            id = postId,
            title = "postTitle",
            content = "postContent",
            image = "postImage",
            categoryId = categoryId,
            date = Date()
        )

        val firstComment = CommentEntity(
            content = "content",
            postId = postId,
            time = Date(),
            writer = "writer",
            writerPw = "writerPw"
        )
        val secondComment = CommentEntity(
            content = "content",
            postId = postId,
            time = Date(),
            writer = "writer",
            writerPw = "writerPw"
        )


        blogDao.insertCategory(categoryEntity)
        blogDao.insertPost(postEntity)

        // when
        blogDao.insertComment(firstComment)
        blogDao.insertComment(secondComment)
        val comments = blogDao.getComments(postId)

        // then
        assertThat(comments[0].postId, `is`(postId))
        assertThat(comments[1].postId, `is`(postId))
    }

    @Test
    @Throws(Exception::class)
    fun deleteComment() {
        // given
        val categoryId = 1
        val categoryEntity = CategoryEntity(
            id = categoryId,
            parentCategoryId = null,
            categoryName = "categoryName"
        )

        val postId = 1
        val postEntity = PostEntity(
            id = postId,
            title = "postTitle",
            content = "postContent",
            image = "postImage",
            categoryId = categoryId,
            date = Date()
        )

        val commentId = 1
        val firstComment = CommentEntity(
            id = commentId,
            content = "content",
            postId = postId,
            time = Date(),
            writer = "writer",
            writerPw = "writerPw"
        )

        blogDao.insertCategory(categoryEntity)
        blogDao.insertPost(postEntity)
        blogDao.insertComment(firstComment)

        // when
        blogDao.deleteComment(firstComment)
        val resultCommentIds = blogDao.getComments(postId)
            .map { it.id }
            .toSet()

        // then
        assertFalse(resultCommentIds.contains(commentId))
    }
}
