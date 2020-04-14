package com.example.petland.pet.creation

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.image.ImageOnCreationActivity
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddPetActivity : AppCompatActivity() {
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date
    lateinit var profileImage : ImageView
    var image : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        profileImage = findViewById(R.id.profileImage)
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = cal.time
                textViewBirthday.text = sdf.format(cal.time)
            }

        textViewBirthday.setOnClickListener {
            val dialog = DatePickerDialog(
                this@AddPetActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }

        profileImage.setOnClickListener { chooseImage() }
    }

    fun createPet(view: View) {
        val currentUser = ParseUser.getCurrentUser()
        val textPetName = findViewById<EditText>(R.id.editTextPetname)
        val chipNumber = findViewById<EditText>(R.id.editTextChip)

        val pet = ParseObject("Pet")
        pet.put("name", textPetName.text.toString())
        pet.put("birthday", date)
        pet.put("chip", Integer.valueOf(chipNumber.text.toString()))
        pet.put("owner", currentUser)
        putImage(pet)
        val relation = pet.getRelation<ParseUser>("caregivers")
        relation.add(currentUser)
        pet.save()

        Log.d(TAG, "Profile created correctly")

        textPetName.text.clear()
        chipNumber.text.clear()

        val intent = Intent(this, HomeActivity::class.java).apply {}
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    private fun putImage(pet: ParseObject) {
        if(image != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            image!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            val file = ParseFile("profileImage.png", bytes)
            file.save()
            pet.put("image", file)
        } else {
            pet.remove("image")
        }
    }

    private fun chooseImage() {
        val intent = Intent(this, ImageOnCreationActivity::class.java).apply {}
        startActivityForResult(intent, ImageOnCreationActivity.PICK_IMAGE)
    }


    fun volver(view: View) {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ImageOnCreationActivity.PICK_IMAGE) {
            if(resultCode == ImageOnCreationActivity.USE_DEFAULT) {
                image = null
                profileImage.setImageDrawable(getDrawable(R.drawable.animal_paw))
            } else if (resultCode == RESULT_OK) {
                val uri = data?.extras?.get("image") as Uri?
                image = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                profileImage.setImageBitmap(image)
            }
        }
    }

    companion object {
        private const val TAG = "Petland AddPet"
    }

}
