package com.smilegate.assignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smilegate.assignment.util.AuthManager
import com.smilegate.assignment.util.ioThread
import java.util.*

@Database(
    entities = [CommentEntity::class, PostEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BlogDatabase : RoomDatabase() {
    abstract val dao: BlogDao

    companion object {
        @Volatile
        private var INSTANCE: BlogDatabase? = null
        val PREPOPULATE_POST = PostEntity(id = 1, title = "요즘 날씨 옷차림은 어떻게 하시나요?", content = "요즘 일교차가 큰데 옷차림은 어떻게 하시나요?")
        val PREPOPULATE_COMMENTs = listOf(
            CommentEntity(id = 1, content = "저는 그래서 요즘 항상 외투를 챙겨다녀요~", postId = 1, writer = "송송", writerPw = AuthManager.encryptPassword("pw"), date = Date()),
            CommentEntity(id = 2, content = "저는 추위를 많이 타서 벌서 패딩을 꺼냈어요.", postId = 2, writer = "춥춥", writerPw = AuthManager.encryptPassword("pw"), date = Date())
        )

        fun getInstance(context: Context): BlogDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BlogDatabase::class.java,
                        "BlogDatabase.db"
                    ).addCallback(getPrepopulateCallback(context.applicationContext))
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }

        private fun getPrepopulateCallback(applicationContext: Context) = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                ioThread {
                    val dao = getInstance(applicationContext).dao
                    dao.insertPreparedPost(PREPOPULATE_POST)
                    dao.insertPreparedComments(PREPOPULATE_COMMENTs)
                }
            }
        }
    }
}