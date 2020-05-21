package com.example.petland.ubications.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.petland.Application.Companion.getAppContext
import com.example.petland.R
import com.example.petland.ubications.enums.PlaceTag
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery


@ParseClassName("Location")
class PetlandLocation : ParseObject() {

    fun getName() : String {
        return getString(NAME) ?: throw NullPointerException()
    }

    fun getAddress() : String {
        return getString(ADDRESS) ?: throw NullPointerException()
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
        val newAvg = if(newNumber == 1) stars.toDouble()
                    else oldAvg + ((stars - oldAvg) / newNumber)
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

    fun getLatLng()  : LatLng {
        val point = getParseGeoPoint(LOCATION) ?: throw NullPointerException()
        return LatLng(point.latitude, point.longitude)
    }

    fun getIcon() : BitmapDescriptor? {
        return when(getPlaceTag()) {
            PlaceTag.RESTAURANT -> bitmapDescriptorFromVector(getAppContext(), R.drawable.map_restaurant_icon)
            PlaceTag.VETERINARY -> bitmapDescriptorFromVector(getAppContext(), R.drawable.map_veterinary_icon)
            PlaceTag.PARK -> bitmapDescriptorFromVector(getAppContext(), R.drawable.map_park_icon)
            PlaceTag.HAIRDRESSER -> bitmapDescriptorFromVector(getAppContext(), R.drawable.map_hairdresser_icon)
            PlaceTag.OTHER -> bitmapDescriptorFromVector(getAppContext(), R.drawable.map_other_icon)
        }
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val background = ContextCompat.getDrawable(context, R.drawable.map_pin_icon)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        if(vectorDrawable != null) {
            val left = (background.intrinsicWidth - vectorDrawable.intrinsicWidth) / 2
            val top = (background.intrinsicHeight - vectorDrawable.intrinsicHeight) / 3
            vectorDrawable.setBounds(
                left,
                top,
                left + vectorDrawable.intrinsicWidth,
                top + vectorDrawable.intrinsicHeight
            )
        }

        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable?.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        private const val OBJECT_NAME = "Location"
        private const val NAME = "name"
        private const val ADDRESS = "address"
        private const val PHONE = "phone_number"
        private const val LINK = "link"
        private const val PLACE_TAG = "tag"
        private const val AVG_STARS = "average_stars"
        private const val N_REVIEWS = "review_count"
        private const val LOCATION = "location"


        fun getAllLocations() : List<PetlandLocation> {
            val query = ParseQuery.getQuery(PetlandLocation::class.java)
            return query.find().toList()
        }
    }
}