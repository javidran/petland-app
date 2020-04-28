package com.example.petland.image

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.R
import com.parse.ParseFile
import com.parse.ParseObject
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_image.*
import java.io.ByteArrayOutputStream

class ImageActivity : AppCompatActivity() {
    private lateinit var parseObject: ParseObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        editImage.setOnClickListener { pickImage() }
        resetImage.setOnClickListener { useDefault() }
        back.setOnClickListener { finish() }

        val intentData = intent.getParcelableExtra<ParseObject>("object")
        if (intentData != null) {
            parseObject = intentData
        } else {
            finish()
        }
        ImageUtils.retrieveImage(parseObject, imageView)
    }

    private fun useDefault() {
        ImageUtils.resetToDefaultImage(parseObject, imageView)
    }

    private fun pickImage() {
        startActivityForResult(ImageUtils.getImageChooserIntent(), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == PICK_IMAGE) {
                Log.d(TAG, "Image chosen")
                val uri = data.data
                if (uri != null) {
                    ImageUtils.startCrop(uri, this)
                }
            } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                Log.d(TAG, "Image cropped")
                val uri = UCrop.getOutput(data)
                if (uri != null) {
                    val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    saveImage(imageBitmap)
                    imageView.setImageBitmap(imageBitmap)
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                Log.d(TAG, "Image not cropped")
                val toast = Toast.makeText(
                    this,
                    getString(R.string.crop_error),
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }

    private fun saveImage(imageBitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()

        Log.d(TAG, "Image converted to ByteArray")

        val file = ParseFile("profileImage.png", bytes)
        file.save()

        Log.d(TAG, "Saving image to server")

        parseObject.put("image", file)
        parseObject.save()

        Log.d(TAG, "Assigning image to user/pet")
    }

    companion object {
        private const val PICK_IMAGE = 1
        private const val TAG = "Petland ImageView"
    }
}
