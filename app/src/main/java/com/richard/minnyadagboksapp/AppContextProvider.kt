package com.richard.minnyadagboksapp

import android.app.Application
import android.content.Context

class AppContextProvider : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}
