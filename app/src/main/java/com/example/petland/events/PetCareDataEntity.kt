package com.example.petland.events

import android.util.Log
import com.parse.ParseClassName
import com.parse.ParseObject
import java.lang.NullPointerException
import java.util.*

@ParseClassName("PetCareData")
class PetCareDataEntity : ParseObject() {
    private var callback : PutMoreDataCallback? = null
        get() = field
        set(value) {
            field = value
        }

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
        return getInt("recurrency") != null
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
        return isDataAvailable("doneDate")
    }

    fun getDoneDate() : Date {
        return getDate("doneDate") ?: throw NullPointerException()
    }

    fun markAsDone(date: Date) {
        if(!isRecurrentlyFinished()) {
            val nextEvent = PetCareDataEntity()

            nextEvent.setPet(getPet())

            val c = Calendar.getInstance()
            c.time = getDate()
            c.add(Calendar.DATE, getRecurrency())
            nextEvent.setDate(c.time)

            nextEvent.setRecurrent(getRecurrency())
            if(hasRecurrencyEndDate()) nextEvent.setRecurrencyEndDate(getRecurrencyEndDate())
            callback?.putChildData(nextEvent)

            nextEvent.saveEvent()
        }

        put("doneDate", date)
        save()
    }

    fun saveEvent() : Boolean{
        if(getParseObject("pet")!=null && getDate("date") != null) {
            save()
            return true
        } else return false
    }
}