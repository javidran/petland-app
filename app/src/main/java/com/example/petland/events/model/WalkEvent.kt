package com.example.petland.events.model

import com.example.petland.mapas.Walk
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("WalkEvent")
class WalkEvent : ParseObject() {

    fun hasWalk() : Boolean {
        return containsKey(WALK)
    }

    fun setWalk(user: Walk) {
        put(WALK, user)
        saveEvent()
    }

    fun getWalk(): Walk {
        val obj = (getParseObject(WALK) ?: throw NullPointerException()) as Walk
        obj.fetch<Walk>()
        return obj
    }

    fun removeWalk() {
        remove(WALK)
    }

    fun saveEvent() {
        save()
    }

    companion object {
        private const val WALK = "walk"

        fun duplicate(oldEvent: WalkEvent) : WalkEvent {
            // A new duplicate of a walk event shouldn't be done, but instead the addition of a new one.
            val newEvent = WalkEvent()
            newEvent.saveEvent()
            return newEvent
        }
    }
}