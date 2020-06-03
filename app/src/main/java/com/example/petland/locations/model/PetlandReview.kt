package com.example.petland.locations.model

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
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

    fun setStars(stars: Float) {
        put(STARS, stars.toDouble())
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

    fun setLocation(location: PetlandLocation) {
        put(LOCATION, location)
    }

    fun getUsername() : String? {
        val creator = getParseUser(USER) ?: throw NullPointerException()
        creator.fetch()
        return creator.getString("name")
    }

    fun setUser( user: ParseUser)  {
        put(USER, user)
    }

    fun saveReview() {
        if(containsKey(USER) && containsKey(LOCATION) ) {
            save()
        } else {
            throw NullPointerException("Some mandatory parameter of PetlandReview is null")
        }
    }

    companion object {

        fun getReviewsLocation(loc: PetlandLocation): List<PetlandReview> {
            val listReview: MutableList<PetlandReview> = ArrayList() //Deberia haber solo 1
            val query = ParseQuery.getQuery(PetlandReview::class.java)
            query.whereEqualTo("location", loc )
            val objects = query.find()
            for(r in objects) {
                listReview.add(r)
            }
            return listReview
        }

        fun getReviewsUserInLocation(u: ParseUser, loc: PetlandLocation): List<PetlandReview> {
            val listReview: MutableList<PetlandReview> = ArrayList() //Deberia haber solo 1
            val query = ParseQuery.getQuery(PetlandReview::class.java)
            query.whereEqualTo("user", u )
            query.whereEqualTo("location", loc )
            val objects = query.find()
            for(r in objects) {
                listReview.add(r)
            }
            return listReview
        }

        private const val OBJECT_NAME = "Review"
        private const val TEXT = "text"
        private const val STARS = "stars"
        private const val DATE = "date"
        private const val USER = "user"
        private const val LOCATION = "location"
    }
}
