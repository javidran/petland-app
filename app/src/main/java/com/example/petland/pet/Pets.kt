package com.example.petland.pet

import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class Pets {

    companion object {
        private lateinit var selectedPet : ParseObject

        fun getPetsFromCurrentUser(): MutableList<ParseObject>? {
            val query = ParseQuery.getQuery<ParseObject>("Pet")
            query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
            val result = query.find()
            selectedPet = result[0]
            return result
        }

        fun hasPets(): Boolean {
            val query = ParseQuery.getQuery<ParseObject>("Pet")
            query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
            val result = query.find()
            selectedPet = result[0]
            return query.find().isNotEmpty()
        }

        fun setSelectedPet(pet: ParseObject) {
           selectedPet = pet
        }

        fun getSelectedPet(): ParseObject {
            if(!this::selectedPet.isInitialized) getPetsFromCurrentUser()
            return selectedPet
        }
    }

}