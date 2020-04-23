package com.example.petland

import android.app.Application
import android.util.Log
import com.example.petland.events.model.*
import com.parse.Parse
import com.parse.ParseObject

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Initializing Parse Server")

        registerEvents()

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(appId)
                .server(parseUrl)
                .build()
        )
        Log.d(TAG, "Parse Server Initialized")
    }


    private fun registerEvents() {
        ParseObject.registerSubclass(FoodEvent::class.java)
        ParseObject.registerSubclass(HygieneEvent::class.java)
        ParseObject.registerSubclass(MeasurementEvent::class.java)
        ParseObject.registerSubclass(MedicineEvent::class.java)
        ParseObject.registerSubclass(PetEvent::class.java)
        ParseObject.registerSubclass(VaccineEvent::class.java)
        ParseObject.registerSubclass(WalkEvent::class.java)
    }

    companion object {
        private const val TAG = "Petland Application"
        private const val appId = "petland"
        private const val parseUrl = "http://petland.sytes.net:1337/parse"
    }

}