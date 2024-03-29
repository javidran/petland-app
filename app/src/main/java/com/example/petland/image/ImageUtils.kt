package com.example.petland.image

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.example.petland.Application
import com.example.petland.R
import com.parse.ParseFile
import com.parse.ParseObject
import com.yalantis.ucrop.UCrop
import java.io.File

class ImageUtils {

    companion object {
        private const val TAG = "Petland ImageUtils"

        fun retrieveImage(profile: ParseObject, imageView: ImageView) {
            val imageFile = profile.get("image") as ParseFile?

            if(imageFile != null) {
                imageFile.getDataInBackground { data, e ->
                    if (e == null) {
                        val bitmapImage = BitmapFactory.decodeByteArray(data, 0, data.size)
                        imageView.setImageBitmap(bitmapImage)
                    } else {
                        Log.d(TAG, "Object doesn't have image associated to its instance.")
                        resetImage(imageView)
                    }
                }
            } else {
                resetImage(imageView)
            }
        }

        private fun resetImage(imageView: ImageView) {
            imageView.setImageDrawable(getDrawable(Application.getAppContext(), R.drawable.animal_paw))
        }

        fun resetToDefaultImage(profile: ParseObject, imageView: ImageView) {
            profile.remove("image")
            profile.saveInBackground()
            resetImage(imageView)
        }

        fun getImageChooserIntent(): Intent {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            return chooserIntent
        }

        fun startCrop(uri: Uri, context: AppCompatActivity) {
            val options = UCrop.Options()
            options.setCircleDimmedLayer(true)
            options.setActiveControlsWidgetColor(
                ContextCompat.getColor(
                    context,
                    R.color.md_blue_500
                )
            )
            options.setToolbarWidgetColor(
                ContextCompat.getColor(
                    context,
                    R.color.md_blue_500
                )
            )

            UCrop.of(uri, Uri.fromFile(File(context.cacheDir, "croppedImage.png")))
                .withAspectRatio(1F, 1F)
                .withMaxResultSize(400, 400)
                .withOptions(options)
                .start(context)
        }
    }
}