package com.example.petland

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.parse.ParseObject
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_editprofile.*
import java.text.SimpleDateFormat
import java.util.*

class AddPetActivity : AppCompatActivity() {
    private val TAG = "Petland AddPet"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        val cal = Calendar.getInstance()
        val user = ParseUser.getCurrentUser()
        editTextUsername.setText(user.get("username").toString())
        editTextEmail.setText(user.get("email").toString())
        val dateb = sdf.format(user.get("birthday"))
        date = user.get("birthday") as Date
        editTextBirthday.setText(dateb.toString())
        editTextName.setText(user.get("name").toString())
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

    }
    fun createPet (view: View) {
        val currentUser = ParseUser.getCurrentUser()
        val textPetName = findViewById<EditText>(R.id.editTextPetname)
        val chipNumber = findViewById<EditText>(R.id.editTextChip)

        val pet = ParseObject("Pet")
        pet.put("name", textPetName.text.toString())
        pet.put("birthday", Calendar.getInstance().time) //Como lo cojo del calendario?
        pet.put("chip", Integer.valueOf(chipNumber.text.toString())) //Funciona?
        pet.put("owner", currentUser)
        val relation = pet.getRelation<ParseUser>("caregivers")
        relation.add(currentUser)
        pet.save()

        Log.d(TAG, "Profile created correctly")

        textPetName.text.clear()
        chipNumber.text.clear()

        val intent = Intent(this, MenuActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


    fun volver(view: View) {
        val intent = Intent(this, MenuActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}
