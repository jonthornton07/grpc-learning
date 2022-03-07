package com.thornton.grpc

import android.app.Application
import timber.log.Timber

class AppApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}