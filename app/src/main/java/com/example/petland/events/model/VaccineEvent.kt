package com.example.petland.events.model

import com.example.petland.R
import com.parse.ParseClassName
import com.parse.ParseObject
import java.lang.NullPointerException

@ParseClassName("VaccineEvent")
class VaccineEvent : ParseObject() {

    fun setName(name : String) {
        put("name", name)
    }

    fun getName() : String {
        return getString("name") ?: throw NullPointerException()
    }

    fun saveEvent() {
        if(getString("name") != null) {
            save()
        } else {
            throw NullPointerException("Algun valor obligatorio de los datos sobre la comida es nulo")
        }
    }
}