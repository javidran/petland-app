package com.example.petland

import android.app.Application
import android.util.Log
import com.parse.Parse

class Application : Application() {
    private val TAG = "Petland Application"

    private val appId = "petland"
    private val parseUrl = "http://dran.sytes.net:1337/parse"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Initializing Parse Server")
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(appId)
            .server(parseUrl)
            .build()
        )
        Log.d(TAG, "Parse Server Initialized")
    }

}