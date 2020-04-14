package com.example.petland.pet

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.image.ImageActivity
import com.example.petland.image.ImageUtils
import com.example.petland.image.ResetImageCallback
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit_pet_profile.*
import kotlinx.android.synthetic.main.activity_edit_pet_profile.textViewName
import kotlinx.android.synthetic.main.activity_edit_pet_profile.textViewOwner
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*

class EditPetProfileActivity : AppCompatActivity(), ResetImageCallback {

    private lateinit var myPet:ParseObject
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet_profile)
        setData()
        profileImage1.setOnClickListener { seeImage() }
    }

    private fun setData() {
        val user = ParseUser.getCurrentUser()
        myPet = intent.extras?.get("petId") as ParseObject
        val caregivers: ParseRelation<ParseUser> = myPet.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query

        textViewName.text = myPet.get("name").toString()

        val textViewBirthday: TextView = findViewById(R.id.editTextBirth)
        val cal = Calendar.getInstance()
        val dateb = sdf.format(myPet.get("birthday"))
        date = myPet.get("birthday") as Date
        editTextBirth.setText(dateb.toString())

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
                this@EditPetProfileActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }


        editTextChip.setText(myPet.get("chip").toString())

        val list = findViewById<TextView>(R.id.petCaregiversList)
        list.text = ""
        listCaregivers.findInBackground { result, e ->
            if (e == null) {
                for(el in result) {
                    list.text = (list.text as String).plus("- ".plus(el.username).plus("\n"))
                    if (el.objectId == myPet.get("owner").toString()) textViewOwner.text = el.username
                }
            } else {
                Log.d(TAG, "PetQuery not completed")
            }
        }
        if (textViewOwner.text == user.username) enableButtons()

        val imageUtils = ImageUtils()
        imageUtils.retrieveImage(myPet, profileImage1, this)
    }

    private fun enableButtons() {
        val deleteButton:TextView = findViewById(R.id.buttonDelete)
        deleteButton.visibility = View.VISIBLE;
        val addCaregivers:TextView = findViewById(R.id.buttonAddCaregivers)
        addCaregivers.visibility = View.VISIBLE;
    }

    fun deletePet(view: View) {
        val petId = myPet.objectId         //Coger id de mascota que quiero borrar
        val query = ParseQuery.getQuery<ParseObject>("Pet")
        query.getInBackground(
            petId
        ) { objectPet, e ->
            if (e == null) {
                // object will be your game score
                objectPet.deleteInBackground {
                    val intent = Intent(this, HomeActivity::class.java).apply {
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            } else {
                Log.d(TAG, "Pet not deleted")
            }
        }
    }

    fun volver(view: View) {
        val intent = Intent(this, ViewPetProfileActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun edit(view: View) {
        myPet.put("chip", Integer.valueOf(editTextChip.text.toString()))
        myPet.put("birthday", date)
        myPet.save()
        volver(view)
    }

    private fun seeImage() {
        val intent = Intent(this, ImageActivity::class.java).apply {}
        intent.putExtra("object", myPet)
        startActivity(intent)
    }

    override fun resetImage() {
        profileImage1.setImageDrawable(this?.getDrawable(R.drawable.animal_paw))
    }

}