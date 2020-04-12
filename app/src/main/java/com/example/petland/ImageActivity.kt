package com.example.petland

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_image.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


private const val PROFILE = "profile"
private const val PICK_IMAGE = 1
private const val TAG = "Petland ImageView"

class ImageActivity : AppCompatActivity() {
    private lateinit var profile: ParseObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        editImage.setOnClickListener { pickImage() }

//        savedInstanceState?.let {
//            profile = it.getParcelable(PROFILE)
//        }

        profile = ParseUser.getCurrentUser()

        getImage()
    }

    private fun pickImage() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null) {
            if (requestCode == PICK_IMAGE) {
                Log.d(TAG, "Image chosen")

                val uri = data.data
                if (uri != null) {
                    val options = UCrop.Options()
                    options.setCircleDimmedLayer(true)
                    options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.md_blue_500))
                    options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.md_blue_500))

                    UCrop.of(uri, Uri.fromFile(File(cacheDir, "croppedImage.png")))
                        .withAspectRatio(1F, 1F)
                        .withMaxResultSize(300, 300)
                        .withOptions(options)
                        .start(this)
                }
            }
            else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                Log.d(TAG, "Image cropped")
                val uri = UCrop.getOutput(data)
                if(uri != null) {
                    val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    saveImage(imageBitmap)
                    imageView.setImageBitmap(imageBitmap)
                }
            }
            else if (resultCode == UCrop.RESULT_ERROR) {
                Log.d(TAG, "Image not cropped")
                val toast = Toast.makeText(this, "Could not bet cropped correctly, please try again", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun getImage() {
        var imageFile = profile.get("image") as ParseFile?

        imageFile?.getDataInBackground { data, e ->
            if (e == null) {
                //                    MediaStore.Images.Media.getBitmap(this.contentResolver, data)
                imageView.setImageBitmap(getBitmapFromByteArray(data))
            } else {
                // something went wrong
            }
        }
    }

    private fun saveImage(imageBitmap :Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        val bytes = byteArrayOutputStream.toByteArray()

        Log.d(TAG, "Image converted to ByteArray")

        val file = ParseFile("profileImage.png", bytes)
        file.save()

        Log.d(TAG, "Saving image to server")

        profile.put("image", file)
        profile.save()

        Log.d(TAG, "Assigning image to user/pet")
    }

    @Throws(IOException::class)
    private fun readBytes(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }

    private fun getBitmapFromByteArray(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }
}
