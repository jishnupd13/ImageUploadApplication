package com.app.mymainapp.application

import android.app.Application
import com.app.mymainapp.BuildConfig
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree


@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        val config: HashMap<String,String> = HashMap()
        config["cloud_name"] = "spellknight"
        config["api_key"] = "434674552768233"
        config["api_secret"] = "pKNWZ2D6PSOxSjA9d1E8x8wH_-4"
        MediaManager.init(this, config)

    }
}
