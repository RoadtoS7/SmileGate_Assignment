package com.smilegate.assignment.di

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BlogApplication: Application() {
    companion object {
        var appContext: Context? = null

        fun getGlobalApplicationContext(): Context {
            checkNotNull(appContext) { "This Application does not inherit com.kakao.GlobalApplication" }
            return appContext!!
        }

        fun getGlobalAppApplication(): BlogApplication {
            checkNotNull(appContext) { "This Application does not inherit com.kakao.GlobalApplication" }
            return appContext!! as BlogApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.DebugTree()
            .also { Timber.plant(it) }
    }
}
