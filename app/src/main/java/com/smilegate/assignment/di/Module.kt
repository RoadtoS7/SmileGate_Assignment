package com.smilegate.assignment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.smilegate.assignment.domain.BlogRepository
import com.smilegate.assignment.db.BlogDao
import com.smilegate.assignment.db.BlogDatabase
import com.smilegate.assignment.ui.base.BaseViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun provideViewModelFactory(): ViewModelProvider.AndroidViewModelFactory = ViewModelFactoryImpl(
        BlogApplication.getGlobalAppApplication()
    )


    /**
     * ViewModelFactory 구현체 (impl) 를 만드는 클래스
     */
    class ViewModelFactoryImpl(
        val blogApplication: BlogApplication,
    ) : ViewModelProvider.AndroidViewModelFactory(blogApplication) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BaseViewModel() as T
        }
    }
}

@GlideModule
class MurengGlide : AppGlideModule()

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBlogDao(): BlogDao {
        val applicationContext = BlogApplication.getGlobalApplicationContext()
        return BlogDatabase.getInstance(applicationContext)
            .dao
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBlogRepository(
        dao: BlogDao
    ): BlogRepository {
        return BlogRepository(dao)
    }
}
