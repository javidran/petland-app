package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("MeasurementEvent")
class MeasurementEvent : ParseObject() {

    fun hasWeight() : Boolean {
        return getDouble("weight") != 0.0
    }

    fun getWeight() : Double {
        return getDouble("weight")
    }

    fun setWeight(weight: Double) {
        put("weight", weight)
    }

    fun removeWeight() {
        remove("weight")
    }

    fun hasHeight() : Boolean {
        return getDouble("height") != 0.0
    }

    fun getHeight() : Double {
        return getDouble("height")
    }

    fun setHeight(height: Double) {
        put("height", height)
    }

    fun removeHeight() {
        remove("Height")
    }

    fun saveEvent() {
        save()
    }
}