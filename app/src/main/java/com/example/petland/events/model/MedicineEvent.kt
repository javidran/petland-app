package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("MedicineEvent")
class MedicineEvent : ParseObject() {

    fun getName() : String {
       return getString("name") ?: throw NullPointerException()
    }

    fun setName(name : String) {
        put("name", name)
    }

    fun getDosage() : Int {
        return getInt("dosage")
    }

    fun setDosage(dosage : Int) {
        put("dosage", dosage)
    }

    fun hasInfo() : Boolean {
        return getString("info") != null
    }

    fun getInfo() : String {
        return getString("info") ?: throw NullPointerException()
    }

    fun setInfo(info : String) {
        put("info", info)
    }

    fun removeInfo() {
        remove("info")
    }

    fun saveEvent() {
        if(getString("name") != null && getInt("dosage") != 0) {
            save()
        } else {
            throw NullPointerException("Some mandatory parameter of MedicineEvent is null")
        }
    }

    companion object {
        fun duplicate(oldEvent: MedicineEvent) : MedicineEvent {
            val newEvent = MedicineEvent()
            newEvent.setName(oldEvent.getName())
            newEvent.setDosage(oldEvent.getDosage())
            if(oldEvent.hasInfo()) newEvent.setInfo(oldEvent.getInfo())
            newEvent.saveEvent()
            return newEvent
        }
    }

}