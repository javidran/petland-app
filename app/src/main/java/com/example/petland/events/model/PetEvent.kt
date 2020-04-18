package com.example.petland.events.model

import android.util.Log
import com.parse.ParseObject
import java.lang.NullPointerException
import java.util.*

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

    fun hasRecurrencyEndDate() : Boolean {
        return getDate("recurrencyUntil") != null
    }

    fun setRecurrencyEndDate(date: Date) {
        put("recurrencyUntil", date)
    }

    fun getRecurrencyEndDate() : Date {
        return getDate("recurrencyUntil") ?: throw NullPointerException()
    }

    fun isRecurrentlyFinished() : Boolean {
        if(isRecurrent()) {
            if (!hasRecurrencyEndDate() || getRecurrencyEndDate() > Calendar.getInstance().time) {
                Log.d("Pet Care Data", "This event is still recurrent")
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
        put("data", dataEvent)
    }

    fun getData() : ParseObject {
        return getParseObject("data") ?: throw NullPointerException()
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

    fun saveEvent() {
        if(getParseObject("pet") != null && getDate("date") != null && getParseObject("data") != null) {
            save()
        } else {
            throw NullPointerException("Algun valor obligatorio del evento es nulo")
        }
    }
}