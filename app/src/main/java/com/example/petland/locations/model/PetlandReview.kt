package com.example.petland.locations.model

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("Review")
class PetlandReview : ParseObject() {

    fun getText() : String {
        return getString(TEXT) ?: throw NullPointerException()
    }

    fun setText(text: String) {
        put(TEXT, text)
    }

    fun getStars() : Float {
        return getDouble(STARS).toFloat()
    }

    fun setStars(stars: Double) {
        put(STARS, stars)
    }

    fun getDate() : Date {
        return getDate(DATE) ?: throw NullPointerException()
    }

    fun setDate(date: Date) {
        put(DATE, date)
    }

    fun getLocation() : PetlandLocation {
        return get(LOCATION) as PetlandLocation
    }

    /*
    fun setLocation(location: PetlandLocation) {
        put(LOCATION, location)
    }
    */

    fun getUser() : ParseUser {
        val obj = getParseUser(USER) ?: throw NullPointerException()
        obj.fetch()
        return obj
    }
/*
    fun getUserName() : String {
        val obj = getParseUser(USER) ?: throw NullPointerException()
        obj.fetch()
        return obj.get("name")
    }

    fun setUser( user: ParseUser)  {
        put(USER, user)
    }
    */

    companion object {
        private const val OBJECT_NAME = "Review"
        private const val TEXT = "text"
        private const val STARS = "stars"
        private const val DATE = "date"
        private const val USER = "user"
        private const val LOCATION = "location"
    }
}
