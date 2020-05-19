package com.example.petland.ubications.model

import com.example.petland.ubications.enums.PlaceTag
import com.parse.ParseClassName
import com.parse.ParseObject


@ParseClassName("Location")
class Location : ParseObject() {

    fun getName() : String {
        return getString(NAME) ?: throw NullPointerException()
    }

    fun getAddress() : String {
        return getString(ADDRESS) ?: throw NullPointerException()
    }

    fun getLocation() : String {
        return getString("name") ?: throw NullPointerException()
    }

    fun getPlaceTag() : PlaceTag {
        val type = getString(PLACE_TAG) ?: throw NullPointerException()
        when(type) {
            "RESTAURANT" -> return PlaceTag.RESTAURANT
            "VETERINARY" -> return PlaceTag.VETERINARY
            "HAIRDRESSER" -> return PlaceTag.HAIRDRESSER
            "PARK" -> return PlaceTag.PARK
        }
        return PlaceTag.OTHER
    }

    fun getAverageStars() : Double {
        return getDouble(AVG_STARS)
    }

    fun getNumberOfReviews() : Int {
        return getInt(N_REVIEWS)
    }

    fun addStars(stars: Int) {
        val oldAvg = getAverageStars()
        val newNumber = getNumberOfReviews() + 1
        val newAvg = oldAvg + ((newNumber - oldAvg) / newNumber)
        put(AVG_STARS, newAvg)
        put(N_REVIEWS, newNumber)
        save()
    }

    fun hasLink() : Boolean {
        return containsKey(LINK)
    }

    fun getLink() : String {
        return getString(LINK) ?: throw NullPointerException()
    }

    fun hasPhoneNumber() : Boolean {
        return containsKey(PHONE)
    }

    fun getPhoneNumber() : Int {
        return getInt(PHONE)
    }

    companion object {
        private const val NAME = "name"
        private const val ADDRESS = "address"
        private const val PHONE = "phone_number"
        private const val LINK = "link"
        private const val PLACE_TAG = "tag"
        private const val AVG_STARS = "average_stars"
        private const val N_REVIEWS = "review_count"
    }
}