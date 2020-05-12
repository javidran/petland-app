package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("WalkEvent")
class WalkEvent : ParseObject() {

    // TODO -> A Walk instance needs to be assigned yet

    fun saveEvent() {
        if( true ) {
            // TODO -> Check if walk event is correctly created
            save()
        } else {
            throw NullPointerException("Some mandatory parameter of WalkEvent is null")
        }
    }

    companion object {
        fun duplicate(oldEvent: WalkEvent) : WalkEvent {
            val newEvent = WalkEvent()
            //TODO -> A new duplicate of a walk event shouldn't be done, but instead the addition of a new one.
            newEvent.saveEvent()
            return newEvent
        }
    }
}