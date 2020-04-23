package com.example.petland.pet

import android.util.Log
import com.example.petland.events.model.PetEvent
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class Pets {

    companion object {
        private val TAG = "Petland Pets"
        private lateinit var selectedPet : ParseObject

        fun getPetsFromCurrentUser(): MutableList<ParseObject> {
            val query = ParseQuery.getQuery<ParseObject>("Pet")
            query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
            val result = query.find()
            selectedPet = result[0]
            return result
        }

        fun userHasPets(): Boolean {
            val query = ParseQuery.getQuery<ParseObject>("Pet")
            query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
            val result = query.find()
            if(result.isNotEmpty()) {
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
    }

}