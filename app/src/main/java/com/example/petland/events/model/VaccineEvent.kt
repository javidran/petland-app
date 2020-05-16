package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject

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
            throw NullPointerException("Some mandatory parameter of VaccineEvent is null")
        }
    }

    companion object {
        fun duplicate(oldEvent: VaccineEvent) : VaccineEvent {
            val newEvent = VaccineEvent()
            newEvent.setName(oldEvent.getName())
            newEvent.saveEvent()
            return newEvent
        }
    }
}