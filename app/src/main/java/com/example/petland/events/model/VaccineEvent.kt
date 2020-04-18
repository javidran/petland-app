package com.example.petland.events

import com.example.petland.R
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("VaccineEvent")
class VaccineEvent : ParseObject() {

    fun setName(name : String) {
        put("name", name)
    }

    fun getName() {
        getString("name")
    }


}