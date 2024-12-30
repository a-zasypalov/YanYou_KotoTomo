package com.gaoyun.yanyou_kototomo

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.gaoyun.yanyou_kototomo.util.ActivityProvider
import com.gaoyun.yanyou_kototomo.util.ThemeChanger
import com.gaoyun.yanyou_kototomo.util.ThemeChangerAndroid
import org.koin.android.ext.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.logger.Level
import org.koin.dsl.module

class YanYouKotoTomoApp : Application(), KoinComponent {
    var initialActivity: Activity? = null

    private val appModule = module {
        single { ActivityProvider(androidApplication(), initialActivity) }
        single<ThemeChanger> { ThemeChangerAndroid(get()) }
    }

    private val activityCallback = object : ActivityLifecycleCallbacks {
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityDestroyed(activity: Activity) {
            (activity as? MainActivity)?.let { _ -> initialActivity = null }

        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            (activity as? MainActivity)?.let { appActivity -> initialActivity = appActivity }
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(activityCallback)

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@YanYouKotoTomoApp)
            modules(appModule)
        }
    }
}