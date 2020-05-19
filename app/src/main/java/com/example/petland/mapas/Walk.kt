package com.example.petland.mapas

import com.parse.ParseClassName
import com.parse.ParseObject


@ParseClassName("Walk")
class Walk  : ParseObject() {
    fun getTime(): Int {
        return getInt("duration")
    }
    fun getRecorrido(): String {
        return getString("distance").toString()
    }
}