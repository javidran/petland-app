package com.example.petland.events.model

import com.parse.ParseClassName
import com.parse.ParseObject
import java.lang.NullPointerException

@ParseClassName("HygieneEvent")
class HygieneEvent : ParseObject() {
    //type

    fun getType() : HygieneType {
        val req = getString("type") ?: throw NullPointerException()
        return HygieneType.valueOf(req)
    }

    fun setType(type: HygieneType) {
        put("type", type.name)
    }

}