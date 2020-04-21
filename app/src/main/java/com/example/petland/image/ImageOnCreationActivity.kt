package com.example.petland.image

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.petland.R
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File

class ImageOnCreationActivity : AppCompatActivity() {
    private var default = true
    private lateinit var imageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        editImage.setOnClickListener { pickImage() }
        resetImage.setOnClickListener { useDefault() }
        back.setOnClickListener { returnInfo() }
    }

    private fun useDefault() {
        default = true
        imageView.setImageDrawable(getDrawable(R.drawable.animal_paw))
    }

    private fun pickImage() {
        val iUtils = ImageUtils()
        startActivityForResult(iUtils.getImageChooserIntent(), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == PICK_IMAGE) {
                Log.d(TAG, "Image chosen")
                val uri = data.data
                if (uri != null) {
                    val iUtils = ImageUtils()
                    iUtils.startCrop(uri, this)
                }
            } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                Log.d(TAG, "Image cropped")
                val uri = UCrop.getOutput(data)
                if (uri != null) {
                    default = false
                    imageUri = uri
                    val image = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageView.setImageBitmap(image)
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

    private fun returnInfo() {
        val intent = Intent()
        if(default) {
            setResult(USE_DEFAULT, intent)
        } else {
            intent.putExtra("image", imageUri)
            setResult(RESULT_OK, intent)
        }
        finish()
    }

    companion object {
        private const val TAG = "Petland ImageView"
        const val PICK_IMAGE = 1
        const val USE_DEFAULT = 100
    }
}
