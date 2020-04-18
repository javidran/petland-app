package com.example.petland.events.model

import com.example.petland.events.enums.HygieneType
import com.parse.ParseClassName
import com.parse.ParseObject
import java.lang.NullPointerException

@ParseClassName("HygieneEvent")
class HygieneEvent : ParseObject() {

    fun getType() : HygieneType {
        val value = getString("type") ?: throw NullPointerException()
        return HygieneType.valueOf(value)
    }

    fun setType(type: HygieneType) {
        put("type", type.name)
    }

    fun saveEvent() {
        if(getString("type") != null) {
            save()
        } else {
            throw NullPointerException("Algun valor obligatorio de los datos sobre la comida es nulo")
        }
    }

}