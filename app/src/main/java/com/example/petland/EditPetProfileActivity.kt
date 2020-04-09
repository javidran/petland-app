package com.example.petland

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit_pet_profile.*
import kotlinx.android.synthetic.main.activity_edit_pet_profile.editTextName
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*

class EditPetProfileActivity : AppCompatActivity() {

    private lateinit var pett:ParseObject
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet_profile)
        setData()
    }

    private fun setData() {
        val namePet = intent.extras?.getString("petName");
        val pets = ParseQuery.getQuery<ParseObject>("Pet")
        pets.whereEqualTo("name", namePet)
        pett = pets.first
        val caregivers: ParseRelation<ParseUser> = pett.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query

        editTextName.setText(pett.get("name").toString())
        editTextOwner.setText(listCaregivers.first.username)
        val dateb = sdf.format(pett.get("birthday"))
/*
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
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
                this@EditPetProfileActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }
        */

        editTextChip.setText(pett.get("chip").toString())

        val list = findViewById<TextView>(R.id.petCaregiversList)
        list.text = ""
        listCaregivers.findInBackground { result, e ->
            if (e == null) {
                for(el in result) {
                    list.text = (list.text as String).plus("- ".plus(el.username).plus("\n"))
                }
            } else {
                Log.d(TAG, "PetQuery not completed")
            }
        }
    }

    fun viewPetProfile(view: View) {
        val intent = Intent(this, ViewPetProfileActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun save(view: View) {
        pett.put("chip", Integer.valueOf(editTextChip.text.toString()))
        pett.save()
        viewPetProfile(view)
    }
}
