package com.example.petland.image

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.parse.ParseFile
import com.parse.ParseObject

class ImageUtils() {

    fun retrieveImage(profile: ParseObject, imageView: ImageView) {
        retrieveImage(profile, imageView, object : ResetImageCallback {
            override fun resetImage() {}
        })
    }

    fun retrieveImage(profile: ParseObject, imageView: ImageView, callback: ResetImageCallback) {
        val imageFile = profile.get("image") as ParseFile?

        if(imageFile != null) {
            imageFile.getDataInBackground { data, e ->
                if (e == null) {
                    val bitmapImage = BitmapFactory.decodeByteArray(data, 0, data.size)
                    imageView.setImageBitmap(bitmapImage)
                } else {
                    Log.d(TAG, "Object doesn't have image associated to its instance.")
                    callback.resetImage()
                }
            }
        } else {
            callback.resetImage()
        }


    }

    fun resetToDefaultImage(profile: ParseObject) {
        profile.remove("image")
        profile.saveInBackground()
    }

    companion object {
        private const val TAG = "Petland ImageUtils"
    }
}