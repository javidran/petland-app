package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("WalkEvent")
class WalkEvent : ParseObject() {

    //A Walk instance needs to be assigned yet


    fun saveEvent() {
        if( true ) {
            save()
        } else {
            throw NullPointerException("Algun valor obligatorio de los datos sobre la comida es nulo")
        }
    }
}