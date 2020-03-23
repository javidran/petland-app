package com.example.petland

import android.app.Application
import android.util.Log
import com.parse.Parse

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("petland", "inicializando parse server")
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId("myAppId")
            .server("http://petland.sytes.net:1337/parse")
            .build()
        )
        Log.d("petland", "parse server inicializado")
    }

}