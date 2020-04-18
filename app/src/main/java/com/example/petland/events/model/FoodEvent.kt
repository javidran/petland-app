package com.example.petland.events.model

import com.example.petland.events.enums.FoodType
import com.example.petland.events.enums.HygieneType
import com.parse.ParseClassName
import com.parse.ParseObject
import java.lang.NullPointerException

@ParseClassName("FoodEvent")
class FoodEvent : ParseObject() {

    fun getType() : FoodType {
        val value = getString("type") ?: throw NullPointerException()
        return FoodType.valueOf(value)
    }

    fun setType(type: HygieneType) {
        put("type", type.name)
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

    fun getAmount() : Int {
        return getInt("amount")
    }

    fun setAmount(amount : Int) {
        put("amount", amount)
    }


    fun saveEvent() {
        if(getString("type") != null && getInt("amount") != 0) {
            save()
        } else {
            throw NullPointerException("Algun valor obligatorio de los datos sobre la comida es nulo")
        }
    }
}