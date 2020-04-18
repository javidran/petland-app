package com.example.petland

import android.app.Application
import android.util.Log
import com.example.petland.events.PetCareDataEntity
import com.parse.Parse
import com.parse.ParseObject




class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Initializing Parse Server")

        ParseObject.registerSubclass(PetCareDataEntity::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(appId)
                .server(parseUrl)
                .build()
        )
        Log.d(TAG, "Parse Server Initialized")
    }

    companion object {
        private const val TAG = "Petland Application"
        private const val appId = "petland"
        private const val parseUrl = "http://petland.sytes.net:1337/parse"
    }

}