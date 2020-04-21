package com.example.petland.pet

import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class Pets {

    fun getPets(): MutableList<ParseObject>? {
        val query = ParseQuery.getQuery<ParseObject>("Pet")
        query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
        return query.find()
    }
    companion object PetsUser {
        private lateinit var selectedPet : ParseObject

        fun getPetsUser(): MutableList<ParseObject>? {
            val query = ParseQuery.getQuery<ParseObject>("Pet")
            query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
            return query.find()
        }
        fun setSelectedPet(pet: ParseObject) {
           selectedPet = pet
        }

        fun getSelectedPet(): ParseObject {
            return selectedPet
        }
    }

}