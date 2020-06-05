package com.example.petland

import AnimalSpecies
import Race
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.petland.events.model.*
import com.example.petland.locations.model.PetlandLocation
import com.example.petland.locations.model.PetlandReview
import com.example.petland.mapas.Walk
import com.example.petland.pet.Pet
import com.example.petland.pet.creation.GetFirstPetActivity
import com.example.petland.user_profile.invitations.Invitation
import com.parse.Parse
import com.parse.ParseObject

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(TAG, "Initializing Parse Server")

        registerEvents()

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(appId)
                .clientKey(clientKey)
                .server(parseUrl)
                .build()
        )
        Log.d(TAG, "Parse Server Initialized")
    }


    private fun registerEvents() {
        ParseObject.registerSubclass(Pet::class.java)
        ParseObject.registerSubclass(FoodEvent::class.java)
        ParseObject.registerSubclass(HygieneEvent::class.java)
        ParseObject.registerSubclass(MeasurementEvent::class.java)
        ParseObject.registerSubclass(MedicineEvent::class.java)
        ParseObject.registerSubclass(PetEvent::class.java)
        ParseObject.registerSubclass(VaccineEvent::class.java)
        ParseObject.registerSubclass(WalkEvent::class.java)
        ParseObject.registerSubclass(Race::class.java)
        ParseObject.registerSubclass(AnimalSpecies::class.java)
        ParseObject.registerSubclass(Walk::class.java)
        ParseObject.registerSubclass(PetlandLocation::class.java)
        ParseObject.registerSubclass(PetlandReview::class.java)
        ParseObject.registerSubclass(Invitation::class.java)

    }

    companion object {
        private const val TAG = "Petland Application"
        private const val appId = "petland"
        private const val clientKey = "clientPetland"
        private const val parseUrl = "http://petland.sytes.net:1337/parse"

        const val INVITATION_NOTIFICATION = "inv_not"
        const val EVENT_NOTIFICATION = "eve_not"
        const val EVENT_NOTIFICATION_INSTANCE = "eve_not_ins"

        private lateinit var instance: Application
        fun getAppContext(): Context = instance.applicationContext

        fun startNoPetsActivity() {
            val context =  getAppContext()
            val intent = Intent(context, GetFirstPetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}