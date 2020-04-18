package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject
import java.lang.NullPointerException

@ParseClassName("MeasurementEvent")
class MeasurementEvent : ParseObject() {

    fun getWeight() : Int {
        return getInt("weight")
    }

    fun setWeight(weight: Int) {
        put("weight", weight)
    }

    fun getHeight() : Int {
        return getInt("height")
    }

    fun setHeight(height: Int) {
        put("height", height)
    }

    fun saveEvent() {
        if(getInt("weight") != 0 && getInt("height") != 0) {
            save()
        } else {
            throw NullPointerException("Algun valor obligatorio de los datos sobre la comida es nulo")
        }
    }
}