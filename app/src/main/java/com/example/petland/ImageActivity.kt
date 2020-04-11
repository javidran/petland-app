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
import java.io.File
import java.io.IOException


private const val PROFILE = "profile"
private const val PICK_IMAGE = 1
private const val TAG = "Petland ImageView"

class ImageActivity : AppCompatActivity() {
    private var profile: ParseObject? = null

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
                val resultUri = UCrop.getOutput(data)
                if(resultUri != null) {
//                    saveImage(resultUri)
//                    val imageBitmap =
//                        MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                    imageView.setImageURI(resultUri)
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
        if(profile != null) {
            var imageFile = profile!!.get("image") as ParseFile?

            imageFile?.getDataInBackground { data, e ->
                if (e == null) {
                    //                    MediaStore.Images.Media.getBitmap(this.contentResolver, data)
                    imageView.setImageBitmap(getImage(data))
                } else {
                    // something went wrong
                }
            }
        }
    }

    private fun saveImage(uri :Uri) {
        if(profile != null) {
            var inputData = readBytes(this, uri)

            val file = ParseFile("profileImage.png", inputData)
            file.save()
            profile!!.put("image", file)
            profile!!.save()
        }
    }

    @Throws(IOException::class)
    private fun readBytes(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }

    fun getImage(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }
}
