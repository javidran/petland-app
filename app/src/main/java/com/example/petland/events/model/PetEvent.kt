package com.example.petland.events.model

import com.example.petland.events.enums.EventType
import com.example.petland.pet.Pets
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
import java.util.*

@ParseClassName("PetEvent")
open class PetEvent : ParseObject() {

    fun getPet() : ParseObject {
        return getParseObject("pet") ?: throw NullPointerException()
    }

    fun setPet(pet: ParseObject) {
        put("pet", pet)
    }

    fun getDate() : Date {
        return getDate("date") ?: throw NullPointerException()
    }

    fun setDate(value : Date) {
        put("date", value)
    }

    fun isRecurrent() : Boolean {
        return getInt("recurrency") != 0
    }

    fun setRecurrent(recurrency: Int) {
        put("recurrency", recurrency)
    }

    fun getRecurrency(): Int {
        return getInt("recurrency")
    }

    fun removeRecurrency() {
        remove("recurrency")
        removeRecurrencyEndDate()
    }


    fun hasRecurrencyEndDate() : Boolean {
        return getDate("recurrencyUntil") != null
    }

    fun setRecurrencyEndDate(date: Date) {
        put("recurrencyUntil", date)
    }

    fun getRecurrencyEndDate() : Date {
        return getDate("recurrencyUntil") ?: throw NullPointerException()
    }

    fun removeRecurrencyEndDate() {
        remove("recurrencyUntil")
    }

    fun isRecurrentlyFinished() : Boolean {
        if(isRecurrent()) {
            if (!hasRecurrencyEndDate() || getRecurrencyEndDate() > Calendar.getInstance().time) {
                return false
            }
        }
        return true
    }

    fun isDone() : Boolean {
        return getDate("doneDate") != null
    }

    fun getDoneDate() : Date {
        return getDate("doneDate") ?: throw NullPointerException()
    }

    fun setData(dataEvent: ParseObject) {
        put("data", dataEvent.objectId)
        put("data_type", dataEvent.className)
    }

    fun getData() : ParseObject {
        val objId = getString("data") ?: throw NullPointerException()
        val className = getString("data_type") ?: throw NullPointerException()
        val query = ParseQuery.getQuery<ParseObject>(className)
        query.whereEqualTo("objectId", objId)
        return query.find().first()
    }

    fun getDataType() : EventType {
        val type = getString("data_type") ?: throw NullPointerException()
        when(type) {
            "FoodEvent" -> return EventType.FOOD
            "HygieneEvent" -> return EventType.HYGIENE
            "MeasurementEvent" -> return EventType.MEASUREMENT
            "MedicineEvent" -> return EventType.MEDICINE
            "VaccineEvent" -> return EventType.VACCINE
            "WalkEvent" -> return EventType.WALK
        }
        return EventType.FOOD
    }

    fun markAsDone(date: Date) {
        if(!isDone()) {
            if (!isRecurrentlyFinished()) {
                val nextEvent = PetEvent()
                nextEvent.setPet(getPet())

                val c = Calendar.getInstance()
                c.time = getDate()
                c.add(Calendar.DATE, getRecurrency())
                nextEvent.setDate(c.time)

                nextEvent.setRecurrent(getRecurrency())
                if (hasRecurrencyEndDate()) nextEvent.setRecurrencyEndDate(getRecurrencyEndDate())
                nextEvent.setData(getData())

                nextEvent.saveEvent()
            }

            put("doneDate", date)
            save()
        }
    }

    fun deleteEvent() {
        getData().deleteInBackground()
        this.deleteInBackground()
    }

    fun saveEvent() {
        if(getParseObject("pet") != null && getDate("date") != null && getString("data") != null) {
            if(getDate() < Calendar.getInstance().time) markAsDone(getDate())
            save()
        } else {
            throw NullPointerException("Some mandatory parameter of PetEvent is null")
        }
    }

    companion object {

        fun getEventsFromPet() : List<PetEvent> {
            val query = ParseQuery.getQuery(PetEvent::class.java)
            query.whereEqualTo("pet", Pets.getSelectedPet())
            return query.find().toList()
        }

        fun getEventsFromPet(pet: ParseObject) : List<PetEvent> {
            val query = ParseQuery.getQuery(PetEvent::class.java)
            query.whereEqualTo("pet", pet)
            return query.find().toList()
        }
    }
}

