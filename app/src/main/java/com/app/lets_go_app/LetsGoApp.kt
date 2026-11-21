package com.app.lets_go_app

import android.app.Application

class LetsGoApp : Application() {
    lateinit var container: DependencyContainer

    override fun onCreate() {
        super.onCreate()
        container = DependencyContainer()
    }
}