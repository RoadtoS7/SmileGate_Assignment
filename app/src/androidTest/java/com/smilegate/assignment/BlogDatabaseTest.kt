package com.smilegate.assignment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smilegate.assignment.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class BlogDatabaseTest {
    private lateinit var blogDao: BlogDao
    private lateinit var db: BlogDatabase

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            this@getOrAwaitValue.observeForever(observer)
        }


        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

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
    fun insertAndGetPost() = runBlocking {
        val postEntity = PostEntity(
            id = 1001,
            title = "postTitle",
            content = "postContent",
            date = Date()
        )

        blogDao.insertPost(postEntity)

        val postEntities = blogDao.getAllPosts()
            .getOrAwaitValue()
            .map { it.id }

        assertTrue(postEntities.size == 1)
        assertTrue(postEntities.contains(postEntity.id))
    }

    @Test
    @Throws(Exception::class)
    fun deletePost() = runBlocking {
        val testPostId = 1001
        val postEntity = PostEntity(
            id = testPostId,
            title = "postTitle",
            content = "postContent",
            date = Date()
        )

        blogDao.insertPost(postEntity)
        blogDao.deletePost(postEntity)

        val postIds = blogDao.getAllPosts()
            .getOrAwaitValue()
            .map { it.id }

        assertFalse(postIds!!.contains(testPostId))
    }

    @Test
    @Throws(Exception::class)
    fun updatePost() = runBlocking {
        // given
        val testPostId = 1001
        val postEntity = PostEntity(
            id = testPostId,
            title = "postTitle",
            content = "postContent",
            date = Date()
        )

        blogDao.insertPost(postEntity)

        val updatedContent = "updatedContent"
        val contentChangedPost = PostEntity(
            id = postEntity.id,
            title = postEntity.title,
            content = updatedContent,
            date = postEntity.date
        )

        // when
        blogDao.updatePost(contentChangedPost)
        val afterPostEntity = blogDao.getAllPosts()
            .getOrAwaitValue()
            .first()

        // then
        assertThat(afterPostEntity.content, `is`(updatedContent))
    }


    @Test
    @Throws(Exception::class)
    fun insertAndGetComments() = runBlocking {
        val postId = 1
        val postEntity = PostEntity(
            id = postId,
            title = "postTitle",
            content = "postContent",
            date = Date()
        )

        val firstComment = CommentEntity(
            content = "content",
            postId = postId,
            writer = "writer",
            writerPw = "writerPw",
            date = Date()
        )
        val secondComment = CommentEntity(
            content = "content",
            postId = postId,
            writer = "writer",
            writerPw = "writerPw",
            date = Date()
        )


        blogDao.insertPost(postEntity)

        // when
        blogDao.insertComment(firstComment)
        blogDao.insertComment(secondComment)
        val comments = blogDao.getComments(postId).value!!

        // then
        assertThat(comments[0].postId, `is`(postId))
        assertThat(comments[1].postId, `is`(postId))
    }

    @Test
    @Throws(Exception::class)
    fun deleteComment() = runBlocking {
        // given
        val postId = 1
        val postEntity = PostEntity(
            id = postId,
            title = "postTitle",
            content = "postContent",
            date = Date()
        )

        val commentId = 1
        val firstComment = CommentEntity(
            id = commentId,
            content = "content",
            postId = postId,
            writer = "writer",
            writerPw = "writerPw",
            date = Date()
        )

        blogDao.insertPost(postEntity)
        blogDao.insertComment(firstComment)

        // when
        blogDao.deleteComment(firstComment)
        val resultCommentIds = blogDao.getComments(postId)
            .value!!
            .map { it.id }
            .toSet()

        // then
        assertFalse(resultCommentIds.contains(commentId))
    }
}
