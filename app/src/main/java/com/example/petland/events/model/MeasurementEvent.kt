package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("MeasurementEvent")
class MeasurementEvent : ParseObject() {

    fun getWeight() : Double {
        return getDouble("weight")
    }

    fun setWeight(weight: Double) {
        put("weight", weight)
    }

    fun getHeight() : Double {
        return getDouble("height")
    }

    fun setHeight(height: Double) {
        put("height", height)
    }

    fun saveEvent() {
        if(getDouble("weight") != 0.0 && getDouble("height") != 0.0) {
            save()
        } else {
            throw NullPointerException("Some mandatory parameter of MeasurementEvent is null")
        }
    }
}