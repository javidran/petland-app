package com.example.petland.pet

import android.util.Log
import com.example.petland.Application
import com.example.petland.events.model.PetEvent
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class Pets {

    companion object {
        private val TAG = "Petland Pets"
        private lateinit var selectedPet : ParseObject

        fun getPetsFromCurrentUser(): List<ParseObject> {
            val query = ParseQuery.getQuery<ParseObject>("Pet")
            query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
            val result = query.find()
            if(result.isEmpty()) {
                Application.startNoPetsActivity()
            }
            else if (!this::selectedPet.isInitialized) {
                selectedPet = result[0]
            }
            return result.toList()
        }

        fun userHasPets(): Boolean {
            val query = ParseQuery.getQuery<ParseObject>("Pet")
            query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
            val result = query.find()
            if(result.isNotEmpty() && !this::selectedPet.isInitialized) {
                selectedPet = result[0]
            }
            return result.isNotEmpty()
        }

        fun setSelectedPet(pet: ParseObject) {
           selectedPet = pet
        }

        fun getSelectedPet(): ParseObject {
            if(!this::selectedPet.isInitialized) getPetsFromCurrentUser()
            return selectedPet
        }

        fun getNamesFromPetList(pets: List<ParseObject>) : Array<String> {
            val names: ArrayList<String> = ArrayList()
            for(p in pets) {
                val name: String = (p.get("name") ?: throw NullPointerException("Pet name should not be null")) as String
                names.add(name)
            }
            val array = arrayOfNulls<String>(pets.size)
            return names.toArray(array)
        }

        fun deletePet(pet: ParseObject) {
            PetEvent.getEventsFromPet(pet).forEach { e -> e.deleteEvent() }
            pet.deleteInBackground { e ->
                if (e == null) {
                    Log.d(TAG, "Pet correctly deleted!")
                } else {
                    Log.d(TAG, "An error occurred while deleting a pet!")
                }
            }
        }

        fun getCaregiversFromPet(pet: ParseObject) : List<ParseUser> {
            return pet.getRelation<ParseUser>("caregivers").query.find()
        }

        fun getCaregiversNamesFromPet(pet: ParseObject) : List<String> {
            val list = pet.getRelation<ParseUser>("caregivers").query.find()
            val names = ArrayList<String>()
            for(l in list) {
                val name = (l.getString("name") ?: throw NullPointerException("Pet name should not be null"))
                names.add(name)
            }
            return names
        }

        fun getVeterinary(pet: ParseObject) : ParseObject? {
            val query = ParseQuery.getQuery<ParseObject>("Location")
            val veterinary = pet.getParseObject("veterinarian")?.objectId ?: return null
            query.whereEqualTo("objectId", veterinary)
            return query.find().first()
        }
    }

}