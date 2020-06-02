package com.example.petland.health

import com.example.petland.pet.Pets
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery

class Veterinary : ParseObject(){

    companion object {
        private lateinit var myVet: ParseObject

        fun getVeterinary(): ParseObject? {
            val pet = Pets.getSelectedPet()
            val query = ParseQuery.getQuery<ParseObject>("Location")
            val veterinary = pet.getParseObject("veterinarian")?.objectId ?: return null
            query.whereEqualTo("objectId", veterinary)
            myVet = query.find().first()
            return myVet
            }

        fun getName(veterinary: ParseObject): String? {
            return myVet.getString("name")
        }

        fun getNumber(veterinary: ParseObject): Number? {
            return veterinary.getNumber("phone_number")
        }

        fun getAdress(veterinary: ParseObject): String? {
            return veterinary.getString("address")
        }

    }


}