package com.example.petland.events.model

import com.example.petland.events.enums.EventType
import com.example.petland.events.enums.FilterEvent
import com.example.petland.pet.Pets
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
import java.util.*

@ParseClassName("PetEvent")
open class PetEvent : ParseObject() {

    fun getPet() : ParseObject {
        val obj = getParseObject(PET) ?: throw NullPointerException()
        obj.fetch<ParseObject>()
        return obj
    }

    fun setPet(pet: ParseObject) {
        put(PET, pet)
    }

    fun getDate() : Date {
        return getDate(DATE) ?: throw NullPointerException()
    }

    fun setDate(value : Date) {
        put(DATE, value)
    }

    fun isRecurrent() : Boolean {
        return getInt(RECURRENT) != 0
    }

    fun setRecurrent(recurrency: Int) {
        put(RECURRENT, recurrency)
    }

    fun getRecurrency(): Int {
        return getInt(RECURRENT)
    }

    fun removeRecurrency() {
        remove(RECURRENT)
        removeRecurrencyEndDate()
    }


    fun hasRecurrencyEndDate() : Boolean {
        return getDate(RECURRENT_UNTIL) != null
    }

    fun setRecurrencyEndDate(date: Date) {
        put(RECURRENT_UNTIL, date)
    }

    fun getRecurrencyEndDate() : Date {
        return getDate(RECURRENT_UNTIL) ?: throw NullPointerException()
    }

    fun removeRecurrencyEndDate() {
        remove(RECURRENT_UNTIL)
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
        return getDate(DONE_DATE) != null
    }

    fun getDoneDate() : Date {
        return getDate(DONE_DATE) ?: throw NullPointerException()
    }

    fun setData(dataEvent: ParseObject) {
        put(DATA, dataEvent.objectId)
        put(DATA_TYPE, dataEvent.className)
    }

    fun getData() : ParseObject {
        val objId = getString(DATA) ?: throw NullPointerException()
        val className = getString(DATA_TYPE) ?: throw NullPointerException()
        val query = ParseQuery.getQuery<ParseObject>(className)
        query.whereEqualTo("objectId", objId)
        return query.find().first()
    }

    fun getDataDuplicate() : ParseObject {
        when(getDataType()) {
            EventType.FOOD -> {
                return FoodEvent.duplicate(getData() as FoodEvent)
            }
            EventType.HYGIENE -> {
                return HygieneEvent.duplicate(getData() as HygieneEvent)
            }
            EventType.MEASUREMENT -> {
                //Every Measurement Event should have different data
                val newEv = MeasurementEvent()
                newEv.saveEvent()
                return newEv
            }
            EventType.MEDICINE -> {
                return MedicineEvent.duplicate(getData() as MedicineEvent)
            }
            EventType.VACCINE -> {
                return VaccineEvent.duplicate(getData() as VaccineEvent)
            }
            EventType.WALK -> {
                return WalkEvent.duplicate(getData() as WalkEvent)
            }
        }
    }

    fun getDataType() : EventType {
        val type = getString(DATA_TYPE) ?: throw NullPointerException()
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

                nextEvent.setData(getDataDuplicate())
                nextEvent.saveEvent()
            }

            put(DONE_DATE, date)
            save()
        }
    }

    fun deleteEvent() {
        getData().delete()
        this.delete()
    }

    fun saveEvent() {
        if(getParseObject(PET) != null && getDate(DATE) != null && getString(DATA) != null) {
            if(getDate() < Calendar.getInstance().time) markAsDone(getDate())
            save()
        } else {
            throw NullPointerException("Some mandatory parameter of PetEvent is null")
        }
    }

    companion object {
        private const val DATE = "date"
        private const val PET = "pet"
        private const val DATA = "data"
        private const val RECURRENT = "recurrent"
        private const val RECURRENT_UNTIL = "recurrent_until"
        private const val DONE_DATE = "done_date"
        private const val DATA_TYPE = "data_type"

        fun getEventsFromPet() : List<PetEvent> {
            return getEventsFromPet(Pets.getSelectedPet(), FilterEvent.NEWEST_FIRST)
        }

        fun getEventsFromPet(filter: FilterEvent) : List<PetEvent> {
            return getEventsFromPet(Pets.getSelectedPet(), filter)
        }

        fun getEventsFromPet(pet: ParseObject) : List<PetEvent> {
            return getEventsFromPet(pet, FilterEvent.NEWEST_FIRST)
        }

        fun getEventsFromPet(pet: ParseObject, filter: FilterEvent) : List<PetEvent> {
            val query = ParseQuery.getQuery(PetEvent::class.java)
            query.whereEqualTo(PET, pet)
            when(filter) {
                FilterEvent.NEWEST_FIRST -> query.orderByDescending(DATE)
                FilterEvent.OLDEST_FIRST -> query.orderByAscending(DATE)
                FilterEvent.ONLY_FOOD -> query.whereEqualTo(DATA_TYPE, "FoodEvent")
                FilterEvent.ONLY_VACCINE -> query.whereEqualTo(DATA_TYPE, "VaccineEvent")
                FilterEvent.ONLY_MEDICINE -> query.whereEqualTo(DATA_TYPE, "MedicineEvent")
                FilterEvent.ONLY_MEASUREMENT -> query.whereEqualTo(DATA_TYPE, "MeasurementEvent")
                FilterEvent.ONLY_HYGIENE -> query.whereEqualTo(DATA_TYPE, "HygieneEvent")
                FilterEvent.ONLY_WALK -> query.whereEqualTo(DATA_TYPE, "WalkEvent")
            }
            return query.find().toList()
        }

        fun getEventsFromUser() : List<PetEvent> {
            val pets = Pets.getPetsFromCurrentUser()
            var events = ArrayList<PetEvent>()

            for(p in pets) {
                events.addAll(getEventsFromPet(p))
            }
            return events
        }
    }
}

